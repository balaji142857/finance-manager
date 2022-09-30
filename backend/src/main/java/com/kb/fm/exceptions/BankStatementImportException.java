package com.kb.fm.exceptions;

public class BankStatementImportException extends Exception {

    public BankStatementImportException(String message) {
        super(message);
    }

    public BankStatementImportException(String message, Exception e) {
        super(message, e);
    }
}
