package com.kb.fm.service;

import java.util.List;

import com.kb.fm.entities.DashboardConfig;
import com.kb.fm.web.model.ChartData;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.ExpenseSearchModel;
import com.kb.fm.web.model.SearchModel;
import com.kb.fm.web.model.SearchResponseModel;

public interface ExpenseService {

	List<ExpenseModel> listExpenses();

	void deleteExpense(Long id);
	
	void addExpenses(List<ExpenseModel> expenses);

	void addExpenseEntities(List<com.kb.fm.entities.Expense> expenses);

	SearchResponseModel<ExpenseModel> filterExpenses(SearchModel<ExpenseSearchModel> filterObj);

	List<com.kb.fm.entities.Expense> getAllExpenses();

	List<ChartData> getChartData(ExpenseSearchModel searchModel, DashboardConfig config);

}
