package com.apriori.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.UUID;

public class DateUtil {
    /**
     * Returns a formatted date and time string
     *
     * @return
     */
    public static String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    /**
     * Extracts current date to be used in various queries
     *
     * @return
     */
    public static String now() {

        String now = "dd-HH-mm-ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(now);
        return (sdf.format(cal.getTime()) + UUID.randomUUID().toString().replace("-", "").substring(0, 5)).toUpperCase();

    }

    /**
     * get Today's date
     * @param dateFormat DateTimeFormatter
     * @return today date.
     */
    public static String getCurrentDate(DateTimeFormatter dateFormat) {
        return dateFormat.format(LocalDateTime.now());
    }
}
