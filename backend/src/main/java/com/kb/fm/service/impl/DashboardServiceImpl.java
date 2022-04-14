package com.kb.fm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kb.fm.entities.DashboardConfig;
import com.kb.fm.repo.DashboardConfigRepository;
import com.kb.fm.service.DashboardService;
import com.kb.fm.service.ExpenseService;
import com.kb.fm.web.model.ChartData;
import com.kb.fm.web.model.ChartModel;
import com.kb.fm.web.model.ExpenseSearchModel;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private DashboardConfigRepository repo;

	@Override
	public ChartModel getChartData(ExpenseSearchModel searchModel) {
		DashboardConfig config = repo.findOneByChartType(searchModel.getChartType());
		List<ChartData> val = expenseService.getChartData(searchModel, config);
		if (CollectionUtils.isEmpty(val)
				|| StringUtils.isEmpty(config.getDateGroupingOracleFormat())) {
			return ChartModel.builder().data(val).title(config.getChartLabel()).build();
		}
		List<ChartData> processed2 = process2(val, config);
		return ChartModel.builder().data(processed2).title(config.getChartLabel()).build();
	}

// Date filtering 
//	label
	private List<ChartData> process2(List<ChartData> expensesByYearMonthDuring, DashboardConfig config) {
		if (CollectionUtils.isEmpty(expensesByYearMonthDuring)
				|| StringUtils.isEmpty(config.getDateGroupingOracleFormat())) {
			return expensesByYearMonthDuring;
		}
		return expensesByYearMonthDuring.stream().sorted(Comparator.comparing(ChartData::getLabel)).map(data -> {
			SimpleDateFormat sdf = new SimpleDateFormat(config.getDateGroupingJavaFormat());
			Date date;
			try {
				date = sdf.parse(data.getLabel());
				SimpleDateFormat sd1 = new SimpleDateFormat(config.getDateLabelFormat());
				String formattedDate = sd1.format(date);
				System.out.println("input:::: "+data.getLabel()+"::::::output:::::"+formattedDate);
				data.setLabel(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}).collect(Collectors.toList());
	}

}
