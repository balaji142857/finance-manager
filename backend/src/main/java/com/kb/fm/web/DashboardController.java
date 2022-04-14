package com.kb.fm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.fm.service.DashboardService;
import com.kb.fm.web.model.ChartModel;
import com.kb.fm.web.model.ExpenseSearchModel;

@RestController
@RequestMapping("dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@PostMapping("{chartType}")
	public ChartModel getChartData(@RequestBody  ExpenseSearchModel searchModel, @PathVariable String chartType) {
		searchModel.setChartType(chartType);
		return dashboardService.getChartData(searchModel);
	}

}
