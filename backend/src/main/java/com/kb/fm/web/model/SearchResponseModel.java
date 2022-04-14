package com.kb.fm.web.model;

import java.util.List;

import lombok.Data;

@Data
public class SearchResponseModel<T> {
	
	List<T> data;
	private Long overallCount;

}
