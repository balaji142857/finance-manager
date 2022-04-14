package com.kb.fm.specs;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.kb.fm.web.model.Options;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationHelper {
	
	public static final int pageSize = 10;
	public static final int pageIndex = 0;
	
	private static final String DESCENDING = Sort.Direction.DESC.name().toLowerCase();
	
	public static  Pageable getPageable(Options options, String defaultField) {
		return PageRequest.of(getPageIndex(options), getPageSize(options), getSortby(options, defaultField));
	}

	private static <T> int getPageSize(Options options) {
		return null != options.getPageSize() ? options.getPageSize().intValue() : pageSize;
	}

	private static <T> int getPageIndex(Options options) {
		return null != options.getPageIndex() ? options.getPageIndex().intValue() : pageIndex;
	}

	private static Sort getSortby(Options options, String defaultField) {
		return Sort.by(new Sort.Order(getDirection(options.getSortDir()), getField(options, defaultField)));
	}

	private static Direction getDirection(String value) {
		return null == value || DESCENDING.equalsIgnoreCase(value) ? Direction.DESC : Direction.ASC;
	}
	
	private static String getField(Options options, String defaultField) {
		return null != options.getSort() ? options.getSort() : defaultField;
	}
	

}
