package com.kb.fm.service;

import com.kb.fm.web.model.ChartModel;
import com.kb.fm.web.model.ExpenseSearchModel;

public interface DashboardService {
	
	ChartModel getChartData(ExpenseSearchModel searchModel);

}
