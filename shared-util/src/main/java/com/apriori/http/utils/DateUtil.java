package com.apriori.http.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

/**
 * The type Date util.
 */
public class DateUtil {

    /**
     * Returns a formatted date and time string
     *
     * @return String timestamp
     */
    public static String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    /**
     * Extracts current date to be used in various queries
     *
     * @return String string
     */
    public static String now() {

        String now = "dd-HH-mm-ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(now);
        return (sdf.format(cal.getTime()) + UUID.randomUUID().toString().replace("-", "").substring(0, 5)).toUpperCase();

    }

    /**
     * Gets today's date
     *
     * @param dateFormat DateTimeFormatter
     * @return today 's date
     */
    public static String getCurrentDate(DateTimeFormatter dateFormat) {
        return dateFormat.format(LocalDateTime.now());
    }

    /**
     * Gets date days after.
     *
     * @param daysCount the days count
     * @param formatter the formatter
     * @return the date days after
     */
    public static String getDateDaysAfter(final int daysCount, DateTimeFormatter formatter) {
        LocalDateTime afterDate = LocalDateTime.now(ZoneOffset.UTC).plusDays(daysCount).withNano(0);
        return formatter.format(afterDate);
    }

    /**
     * Gets date days before.
     *
     * @param daysCount the days count
     * @param formatter the formatter
     * @return the date days before
     */
    public static String getDateDaysBefore(final int daysCount, DateTimeFormatter formatter) {
        LocalDateTime afterDate = LocalDateTime.now(ZoneOffset.UTC).minusDays(daysCount).withNano(0);
        return formatter.format(afterDate);
    }

    /**
     * Get custom date in milliseconds
     *
     * @param customDate
     * @return
     */
    @SneakyThrows
    public static Long getDateInMilliSeconds(String customDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.parse(customDate).getTime();
    }

    /**
     * Utility function to convert java Date to TimeZone format
     *
     * @param customDate date in mm/dd/yyyy format
     * @param timeZone   - PST or EST
     * @return Date in milliseconds
     */
    @SneakyThrows
    public static Long getDateInMilliSeconds(String customDate, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (StringUtils.isEmpty(timeZone)) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        // return Date in required format with timezone as String
        return format.parse(customDate).getTime();
    }

    /**
     * Gets date from two months ago
     *
     * @return String date two months ago
     */
    public String getDateTwoMonthsAgo() {
        return getDateMonthsAgo(2);
    }

    private String getDateMonthsAgo(final int monthsCount) {
        LocalDateTime pastDate = LocalDateTime.now(ZoneOffset.UTC).minusMonths(monthsCount).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(pastDate);
    }
}