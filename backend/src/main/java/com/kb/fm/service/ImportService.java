package com.kb.fm.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kb.fm.exceptions.FinanceManagerException;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.GenericResponse;

public interface ImportService {

	void configureApplication();

	GenericResponse<List<ExpenseModel>> loadExpensesFromFile(MultipartFile[] uploadedFiles) throws FinanceManagerException;

	void importExpenses(List<ExpenseModel> expenseList);

}
