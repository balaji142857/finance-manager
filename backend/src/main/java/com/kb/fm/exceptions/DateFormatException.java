package com.kb.fm.exceptions;

import java.text.ParseException;

public class DateFormatException extends Exception {

    public DateFormatException(String message, ParseException e) {
        super(message);
    }
}
