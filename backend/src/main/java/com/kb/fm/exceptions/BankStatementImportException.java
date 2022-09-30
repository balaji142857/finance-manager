package com.kb.fm.exceptions;

public class BankStatementImportException extends Exception {

    private final String fileName;

    public BankStatementImportException(String fileName, String message) {
        super(message);
        this.fileName = fileName;
    }

    public BankStatementImportException(String fileName, String message, Exception e) {
        super(message, e);
        this.fileName = fileName;
    }

    @Override
    public String getMessage() {
        return fileName + ": " + super.getMessage();
    }
}
