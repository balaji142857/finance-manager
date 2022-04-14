package com.kb.fm.exceptions;

public class FinanceManagerException extends Exception {
	
	public FinanceManagerException(String message) {
		super(message);
	}
	
	public FinanceManagerException(Exception e) {
		super(e);
	}

}
