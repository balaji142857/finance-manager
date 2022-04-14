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

	public void deleteExpense(Long id);
	
	public void addExpenses(List<ExpenseModel> expenses);

	public void addExpenseEntities(List<com.kb.fm.entities.Expense> expenses);

	public SearchResponseModel<ExpenseModel> filterExpenses(SearchModel<ExpenseSearchModel> filterObj);

	List<com.kb.fm.entities.Expense> getAllExpenses();

	List<ChartData> getChartData(ExpenseSearchModel searchModel, DashboardConfig config);

}
