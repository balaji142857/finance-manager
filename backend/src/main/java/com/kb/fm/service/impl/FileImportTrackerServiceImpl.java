package com.kb.fm.service.impl;

import com.kb.fm.entities.FileImportMetadata;
import com.kb.fm.repo.FileImportTrackerRepo;
import com.kb.fm.service.AssetService;
import com.kb.fm.service.FileImportTrackerService;
import com.kb.fm.util.StorageUtil;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.kb.fm.util.Constants.FLAG_NO;

@RequiredArgsConstructor
@Service
@Slf4j
@Data
public class FileImportTrackerServiceImpl implements FileImportTrackerService {

    private final FileImportTrackerRepo repo;
    private final AssetService assetService;
    @Value("${app.storage.file.import.temp.location}")
    private String fileTempStorageLocation;
    @Value("${app.storage.file.import.location}")
    private String fileStorageLocation;
    @Value("${app.fileImport.diskStore.skip}")
    private boolean skipDiskStorage;

    @Override
    public void trackImport(List<BankMultipartFileWrapper> files) {
        if (CollectionUtils.isEmpty(files)) {
            log.warn("Nothing to track, no files present");
            return;
        }
        List<FileImportMetadata> entities = mapToEntity(files);
        repo.saveAll(entities);
        for(var i =0; i < entities.size(); i++) {
            files.get(i).setImportId(entities.get(i).getId());
        }
        if (!skipDiskStorage) {
            saveFilesToDisk(files, fileTempStorageLocation);
            log.info("Saved the imported files to temp location {}", fileTempStorageLocation);
        }
    }

    @Override
    public void trackVerification(List<Long> fileIds) {
        if (skipDiskStorage) {
            log.warn("Disk storage is disabled, skipping");
            return;
        }
        List<FileImportMetadata> files = repo.findAllById(fileIds);
        for(var file: files) {
            StorageUtil.moveFile(fileTempStorageLocation, fileStorageLocation, file);
        }
    }

    private FileImportMetadata map(BankMultipartFileWrapper fileWrapper) {
        return FileImportMetadata.builder()
        .asset(assetService.getAsset(fileWrapper.getBankName()))
        .fileName(fileWrapper.getFile().getOriginalFilename())
        .isDataImported(FLAG_NO)
        .tempStorage(fileTempStorageLocation)
        .build();
    }

    private void saveFilesToDisk(List<BankMultipartFileWrapper> files, String directory) {
        for(var file : files) {
            StorageUtil.saveFileToDisk(file, directory);
        }
    }

    private List<FileImportMetadata> mapToEntity(List<BankMultipartFileWrapper> files) {
        return files.stream().map(this::map).toList();
    }
}
