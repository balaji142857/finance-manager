package com.kb.fm.web.model.imports;

import lombok.Data;

@Data
public class ColumnModel {
	
	private String columnName;
	private Integer columnIndex;
	private String columnFormat;
	private Integer readOrder;

}
