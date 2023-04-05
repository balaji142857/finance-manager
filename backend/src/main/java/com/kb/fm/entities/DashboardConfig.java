package com.kb.fm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class DashboardConfig {
	
	@Id
	private Long id;
	private String chartType;
	private String dateGroupingOracleFormat;
	private String dateGroupingJavaFormat;	
	private String dateLabelFormat;
	private String chartLabel;

}
