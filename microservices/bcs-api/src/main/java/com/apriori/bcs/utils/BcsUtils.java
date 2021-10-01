package com.apriori.bcs.utils;

import static org.junit.Assert.fail;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.utils.ApiUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BcsUtils extends ApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(BcsUtils.class);

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
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
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
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
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
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        return value;
    }

    /**
     * Cancel any batch in a non-terminal state
     *
     * @param batch
     * @return
     */
    public static boolean checkAndCancelBatch(Batch batch) {
        if (!State.contains(batch.getState())) {
            BatchResources.cancelBatchProccessing(batch.getIdentity());
            return true;
        }
        return false;
    }

    /**
     * Wait for a batch to enter into a terminal state
     *
     * @param batch
     * @return
     * @throws InterruptedException
     */
    public static State waitingForBatchProcessingComplete(Batch batch) throws InterruptedException {
        Object batchDetails;
        Integer pollingInterval = 0;
        State state;

        while (pollingInterval <= Constants.BATCH_POLLING_TIMEOUT) {
            batchDetails = BatchResources.getBatchRepresentation(batch.getIdentity()).getResponseEntity();
            try {
                state = pollState(batchDetails, Batch.class);
                if (state != State.PROCESSING) {
                    return state;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;

            }

            pollingInterval += 1;
        }

        return State.PROCESSING;
    }

    /**
     * Polls BCS to get a batch/part's costing status
     *
     * @param obj
     * @param klass
     * @return Costing Status
     */
    public static State pollState(Object obj, Class klass) throws InterruptedException {
        String state = BcsUtils.getState(obj, klass);

        // TODO ALL: should be refactored to switch
        if (state.toUpperCase().equals("COMPLETED")) {
            return State.COMPLETED;
        } else if (state.toUpperCase().equals("ERRORED")) {
            return State.ERRORED;
        } else if (state.toUpperCase().equals("REJECTED")) {
            return State.REJECTED;
        } else if (state.toUpperCase().equals("CANCELED")) {
            return State.CANCELED;
        } else {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        }

        return State.PROCESSING;
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
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
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
                logger.error(errors);
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
