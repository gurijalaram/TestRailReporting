package com.apriori.apibase.services.cis;

import com.apriori.apibase.utils.ApiUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CisUtils extends ApiUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CisUtils.class);

    public static String getIdentity(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getIdentity");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static String getState(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getState");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static String getErrors(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getErrors");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static String getReportIdentity(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getReportIdentity");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    public static Boolean pollState(Object obj, Class klass) {
        String state;
        try {
            state = CisUtils.getState(obj, klass);

            if (state.toUpperCase().equals("COMPLETED")) {
                return true;
            } else {
                Thread.sleep(10000);
            }
        }  catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }
}
