package com.apriori.cis.utils;

import com.apriori.apibase.utils.ApiUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CisUtils extends ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(CisUtils.class);

    public static String getIdentity(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getIdentity");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static String getState(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getState");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static String getReportIdentity(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getReportIdentity");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }
}
