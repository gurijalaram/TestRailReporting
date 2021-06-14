package com.apriori.bcs.utils;

import com.apriori.apibase.utils.ApiUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class BcsUtils extends ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(BcsUtils.class);

    public enum State {
        COMPLETE,
        ERRORED,
        PROCESSING
    }

    /**
     * Get the entity's identity
     *
     * @param obj Entity
     * @param klass
     * @return string
     */
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

    /**
     * Get the entity's state
     *
     * @param obj Entity
     * @param klass
     * @return string
     */
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

    /**
     * Check for and return any error message
     *
     * @param obj Entity
     * @param klass
     * @return string
     */
    public static String getErrors(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getErrors");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    /**
     * Gets the provided report identity
     *
     * @param obj Report
     * @param klass
     * @return string
     */
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

    /**
     * Checks the state of the provided entity
     *
     * @param obj Entity to check state for
     * @param klass
     * @return Entity State
     */
    public static State pollState(Object obj, Class klass) {
        String state;
        try {
            state = BcsUtils.getState(obj, klass);

            if (state.toUpperCase().equals("ERRORED")) {
                logger.debug("Part process has errored");
                return State.ERRORED;
            }

            if (state.toUpperCase().equals("COMPLETED")) {
                return State.COMPLETE;
            } else {
                Thread.sleep(10000);
            }
        }  catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return State.PROCESSING;
    }
}
