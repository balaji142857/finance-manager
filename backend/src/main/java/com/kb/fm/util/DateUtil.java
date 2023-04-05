package com.kb.fm.util;

import com.kb.fm.exceptions.DateFormatException;
import com.kb.fm.exceptions.FinanceManagerException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@UtilityClass
@Slf4j
public class DateUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final List<String> DEFAULT_EXCEL_INPUT_FORAMTS = List.of("dd-MM-yyyy", "dd/MM/yyyy", "yyyyMMdd");

	public static Date convert(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			throw new FinanceManagerException("Could not parse date " + input+" with format: "+DATE_FORMAT);
		}
	}

	public static String convertDateToDatePickerFormat(Date date) {
		return new SimpleDateFormat(DATE_FORMAT).format(date);
	}

	public static Date convertToDate(String input) {
		return convertToDate(input, DEFAULT_EXCEL_INPUT_FORAMTS);
	}
	
	public static Date convertToDate(String input, String format) throws DateFormatException {
		try {
			return new SimpleDateFormat(format).parse(input);
		} catch (ParseException e) {
			throw new DateFormatException("Could not parse the input: " + input +" with format: " + format, e);
		}
	}

	public static Date convertToDate(String input, List<String> acceptedFormats) {
		if (CollectionUtils.isEmpty(acceptedFormats)) {
			throw new FinanceManagerException("At least one date format is required");
		}
		for (String format : acceptedFormats) {
			try {
				convertToDate(input, format);
			} catch (Exception e) {
				log.warn("parsing of date {} with format {} failed", input, format);
			}
		}
		log.error("Date: {} parsing failed with the registered formats: {}", input, acceptedFormats);
		throw new FinanceManagerException("Unable to parse the date " + input + " with the given formats");
	}

}
