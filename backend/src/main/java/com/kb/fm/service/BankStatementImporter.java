package com.kb.fm.service;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;

import java.util.List;

public interface BankStatementImporter {

    List<ExpenseModel> importBankStatement(BankMultipartFileWrapper fileWrapper) throws BankStatementImportException;

    String getBankName();
}
