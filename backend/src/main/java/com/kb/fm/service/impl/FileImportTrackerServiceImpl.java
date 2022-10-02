package com.kb.fm.service.impl;

import com.kb.fm.entities.FileImportMetadata;
import com.kb.fm.exceptions.FileStorageException;
import com.kb.fm.repo.FileImportTrackerRepo;
import com.kb.fm.service.AssetService;
import com.kb.fm.service.FileImportTrackerService;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.kb.fm.util.Constants.FLAG_NO;

@RequiredArgsConstructor
@Service
@Slf4j
@Data
public class FileImportTrackerServiceImpl implements FileImportTrackerService {

    private final FileImportTrackerRepo repo;
    private final AssetService assetService;
    @Value("${storage.file.import.temp.location}")
    private String fileTempStorageLocation;
    @Value("${storage.file.import.location}")
    private String fileStorageLocation;
    @Value("${app.fileImport.diskStore.skip}")
    private boolean skipDiskStorage;

    @Override
    public void trackImport(List<BankMultipartFileWrapper> files) {
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

    private List<FileImportMetadata> mapToEntity(List<BankMultipartFileWrapper> files) {
        return files.stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public void trackVerification(List<Long> fileIds) {
        if (skipDiskStorage) {
            log.warn("Disk storage is disabled, skipping");
            return;
        }
        List<FileImportMetadata> files = repo.findAllById(fileIds);
        for(var file: files) {
            moveFile(fileTempStorageLocation, fileStorageLocation, file);
        }
    }

    private FileImportMetadata map(BankMultipartFileWrapper fileWrapper) {
        FileImportMetadata entity = new FileImportMetadata();
        entity.setAsset(assetService.getAsset(fileWrapper.getBankName()));
        entity.setFileName(fileWrapper.getFile().getOriginalFilename());
        entity.setIsDataImported(FLAG_NO);
        entity.setTempStorage(fileTempStorageLocation);
        return entity;
    }

    private void saveFilesToDisk(List<BankMultipartFileWrapper> files, String directory) {
        for(var file : files) {
            saveFileToDisk(file, directory);
        }
    }

    //TODO move this to a generic file helper
    //TODO check if this works via docker
    private void saveFileToDisk(BankMultipartFileWrapper file, String directory) {
        try {
            Path tempStorage = Paths.get(directory);
            Files.createDirectories(tempStorage);
            Path tempFile = tempStorage.resolve(file.getFile().getOriginalFilename());
            Path fileTempPath = Files.createFile(tempFile);
            Files.write(fileTempPath, file.getFile().getBytes());
        } catch (FileAlreadyExistsException e) {
            try {
                Files.delete(Paths.get(directory, file.getFile().getOriginalFilename()));
            } catch (IOException ioException) {
              throw new FileStorageException("File with name " + file.getFile().getName()+ "already exists," +
                      " attempt to delete it from " +directory+ " failed", ioException);
            }
        } catch (IOException e) {
            throw new FileStorageException("Unable to save the files to disk", e);
        }
    }

    private void moveFile(String from, String to, FileImportMetadata file) {
        try {
            Path source = Path.of(from, file.getFileName());
            Path destination = Path.of(to, file.getFileName());
            Files.createDirectories(Paths.get(to));
            Files.move(source, destination);
        } catch (IOException e) {
            throw new FileStorageException("Unable to move the imported file to storage location: " + to, e);
        }
    }
}
