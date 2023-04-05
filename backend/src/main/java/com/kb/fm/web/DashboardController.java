package com.kb.fm.web;

import com.kb.fm.service.DashboardService;
import com.kb.fm.web.model.ChartModel;
import com.kb.fm.web.model.ExpenseSearchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("dashboard")
public class DashboardController {
	
	private final DashboardService dashboardService;
	
	@PostMapping("{chartType}")
	public ChartModel getChartData(@RequestBody  ExpenseSearchModel searchModel, @PathVariable String chartType) {
		searchModel.setChartType(chartType);
		return dashboardService.getChartData(searchModel);
	}

}
