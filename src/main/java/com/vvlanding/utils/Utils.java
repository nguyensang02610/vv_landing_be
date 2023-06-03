package com.vvlanding.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static Date string2Date(String dateString) {
        try {

            if (dateString != null && dateString.length() > 0) {
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date parse = dateFormat.parse(dateString);
                return parse;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    public static Double stringToDouble(String number) {
        try {
            if (number == null || number.length() == 0) {
                return null;
            }

            Number parse = NumberFormat.getNumberInstance(java.util.Locale.ITALY).parse(number);
            return parse.doubleValue();
        } catch (ParseException e) {
            return null;
        }
    }
}
