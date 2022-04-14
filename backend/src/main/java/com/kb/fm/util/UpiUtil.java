package com.kb.fm.util;

import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpiUtil {
	
	private static final String UPI = "UPI";
	
	public static String getUpiIdFromTransaction(String transactionFormat, String input) {
		if (!StringUtils.hasLength(input) || "ICICI".equalsIgnoreCase(transactionFormat)  && !input.startsWith(UPI)) {
			return null;
		}
		String[] divisions = input.split("/");
		if (divisions.length < 4)
			 return null;
		return divisions[3];
	}

}
