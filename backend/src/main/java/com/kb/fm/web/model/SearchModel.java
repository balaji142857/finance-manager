package com.kb.fm.web.model;

import lombok.Data;

@Data
public class SearchModel<T> {
	
	private Options options;
	private  T data;

}
