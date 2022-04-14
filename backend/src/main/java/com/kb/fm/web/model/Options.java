package com.kb.fm.web.model;

import lombok.Data;

@Data
public class Options {
	
	private Long pageSize;
	private Long pageIndex;
	private String sort;
	private String sortDir;
}
