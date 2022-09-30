package com.kb.fm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.kb.fm.exceptions.DateFormatException;
import org.springframework.util.CollectionUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class DateUtil {
	private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static List<String> defaultExcelInputForamts = Arrays.asList("dd-MM-yyyy", "dd/MM/yyyy", "yyyyMMdd");

	public static Date convert(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid date " + input);
		}
	}

	public static String convertDateToDatePickerFormat(Date date) {
		return new SimpleDateFormat(dateFormat).format(date);
	}

	public static Date convertToDate(String input) {
		return convertToDate(input, defaultExcelInputForamts);
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
			throw new RuntimeException("At least one date format is required");
		}
		for (String format : acceptedFormats) {
			try {
				convertToDate(input, format);
			} catch (Exception e) {
				log.warn("parsing of date {} with format {} failed", input, format);
			}
		}
		throw new RuntimeException("Unable to parse the date " + input + " with the given formats");
	}

}
