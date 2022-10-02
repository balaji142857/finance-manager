package com.kb.fm.exceptions;

import java.io.IOException;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message, IOException e) {
        super(message, e);
    }

}
