package com.kb.fm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
