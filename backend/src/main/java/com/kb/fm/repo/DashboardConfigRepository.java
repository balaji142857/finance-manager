package com.kb.fm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.fm.entities.DashboardConfig;

@Repository
public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, Long>{
	
	DashboardConfig findOneByChartType(String chartType);

}
