package com.kb.fm.util;

import com.kb.fm.entities.FileImportMetadata;
import com.kb.fm.exceptions.FileStorageException;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
@Slf4j
public class StorageUtil {

    //FIXME will fail in multi node env unless directory is some sort of shared drive
    public static void saveFileToDisk(BankMultipartFileWrapper file, String directory) {
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

    public void moveFile(String from, String to, FileImportMetadata file) {
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
