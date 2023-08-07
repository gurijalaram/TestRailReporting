package com.apriori.bcs.utils;

import static org.junit.jupiter.api.Assertions.fail;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.models.response.Part;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BcsUtils extends ApiUtil {


    public static String convertSecsToMins(long secs) {
        long minutes = (secs % 3600) / 60;
        long seconds = secs % 60;
        return minutes + " minutes " + seconds + " seconds";
    }

    public enum State {
        COMPLETED("COMPLETED"),
        COSTING("COSTING"),
        ERRORED("ERRORED"),
        PROCESSING("PROCESSING"),
        REJECTED("REJECTED"),
        CANCELED("CANCELLED");

        private final String state;

        State(String st) {
            state = st;
        }

        static boolean contains(String objectsState) {
            for (State state : State.values()) {
                if (state.name().equals(objectsState)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static Object[][] convertMapToArray(HashMap<String, Object> hashMap) {
        Object[][] arr =
            hashMap.entrySet().stream()
                .map(e -> new Object[] {e.getKey(), e.getValue()})
                .toArray(Object[][]::new);
        return arr;
    }

    /**
     * Get the entity's identity
     *
     * @param obj   Entity
     * @param klass
     * @return string
     */
    public static String getIdentity(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getIdentity");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    /**
     * Get the entity's state
     *
     * @param obj   Entity
     * @param klass
     * @return string
     */
    public static String getState(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getState");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    /**
     * Check for and return any error message
     *
     * @param obj   Entity
     * @param klass
     * @return string
     */
    public static String getErrors(Object obj, Class klass) {
        String value = null;
        try {
            value = getPropertyValue(obj, klass, "getErrors");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    /**
     * Polls BCS to get a batch/part's costing status
     *
     * @param obj
     * @param klass
     * @return Costing Status
     */
    public static State pollForCostingState(Object obj, Class klass) throws InterruptedException {
        String state = BcsUtils.getState(obj, klass);
        if (state.toUpperCase().equals("COSTING")) {
            return State.COSTING;
        } else {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        }

        return State.PROCESSING;
    }

    /**
     * Parses the Scenario & Component
     *
     * @param url THe cid url returned in the getPart response
     * @return
     */
    public static Map<String, String> getScenarioAndComponent(String url) {
        String[] sections = url.split("/");
        Map<String, String> identities = new HashMap<>();
        identities.put("component", sections[4]);
        identities.put("scenario", sections[6]);
        return identities;
    }

    /**
     * Wait for a part to enter into a costing state
     */
    public static void waitForCostingState(String batchIdentity, String partIdentity) {
        BcsUtils.State partState = BcsUtils.State.PROCESSING;
        int count = 0;
        int defaultTimeout = 100;
        Object partDetails;

        while (count <= defaultTimeout) {
            partDetails =
                BatchPartResources.getBatchPartRepresentation(batchIdentity,
                    partIdentity);
            try {
                partState = BcsUtils.pollForCostingState(partDetails, Part.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String state = BcsUtils.getState(partDetails, Part.class);
            if (state.equalsIgnoreCase(BcsUtils.State.ERRORED.toString())) {
                String errors = BcsUtils.getErrors(batchIdentity, Part.class);
                log.error(errors);
                fail("Part was in state 'ERRORED'");
                return;
            }

            if (partState == BcsUtils.State.COSTING) {
                break;
            }
            count += 1;
        }
    }
}
