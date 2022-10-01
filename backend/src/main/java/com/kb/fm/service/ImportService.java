package com.kb.fm.service;

import java.util.List;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;

import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.GenericResponse;

public interface ImportService {

	GenericResponse<List<ExpenseModel>> readBankStatements(List<BankMultipartFileWrapper> files) throws BankStatementImportException;

	void importExpenses(List<ExpenseModel> expenseList);

}
