package com.apriori.bcs.api.controller;

import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.models.request.batch.BatchProperties;
import com.apriori.bcs.api.models.request.batch.BatchRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Batches;
import com.apriori.bcs.api.models.response.Cancel;
import com.apriori.bcs.api.utils.BcsBase;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class contains methods Utils related Batch-Controller APIs.
 * <li>Get Batches
 * <li>Create Batch
 * <li>Get Batch with Batch ID
 * <li>Cancel Batch
 * <li>Start-Costing
 */
@Slf4j
public class BatchResources extends BcsBase {

    /**
     * Creates Batch
     *
     * @return Batch response instance
     */
    public static ResponseWrapper<Batch> createBatch() {
        long currentMillis = System.currentTimeMillis();
        Map<String, String> headerInfo = new HashMap<>();
        headerInfo.put("Accept", "*/*");
        headerInfo.put("Content-Type", "application/json");
        BatchRequest newBatchRequest = BatchRequest.builder().batch(BatchProperties.builder()
                .externalId("auto-External-" + currentMillis)
                .exportSetName("auto-ExportSet-" + currentMillis)
                .rollupName("auto-RollUp-" + currentMillis)
                .rollupScenarioName("auto-Scenario-" + currentMillis)
                .build())
            .build();
        final RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCHES, Batch.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .headers(headerInfo)
            .body(newBatchRequest)
            .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates Batch with invalid customer data
     *
     * @return Batch response instance
     */
    public static <T> ResponseWrapper<T> createBatch(String customerIdentity, Class<T> klass) {
        long currentMillis = System.currentTimeMillis();
        Map<String, String> headerInfo = new HashMap<>();
        headerInfo.put("Accept", "*/*");
        headerInfo.put("Content-Type", "application/json");
        BatchRequest newBatchRequest = BatchRequest.builder().batch(BatchProperties.builder()
                .externalId("auto-External-" + currentMillis)
                .exportSetName("auto-ExportSet-" + currentMillis)
                .rollupName("auto-RollUp-" + currentMillis)
                .rollupScenarioName("auto-Scenario-" + currentMillis)
                .build())
            .build();
        final RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCHES, klass)
            .inlineVariables(customerIdentity)
            .headers(headerInfo)
            .body(newBatchRequest)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Returns list of batches
     *
     * @return Response Object
     */
    public static ResponseWrapper<Batches> getBatches() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCHES, Batches.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get single batch with batch id
     *
     * @param identity - Batch Id
     * @return - response
     */
    public static ResponseWrapper<Batch> getBatchRepresentation(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, Batch.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), identity)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get single batch with batch id with no logging information
     *
     * @param identity - Batch Id
     * @return - response
     */
    public static ResponseWrapper<Batch> getBatchWithNoLogInfo(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, Batch.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), identity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Costing a batch
     *
     * @param batch - Batch ID
     * @return - response
     */
    public static ResponseWrapper<String> startBatchCosting(Batch batch) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.START_COSTING_BY_ID, null)
            .inlineVariables(PropertiesContext.get("customer_identity"), batch.getIdentity())
            .customBody("{}")
            .expectedResponseCode(HttpStatus.SC_ACCEPTED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Cancel the batch
     *
     * @param batchIdentity - Batch ID
     * @return - response
     */
    public static ResponseWrapper<Cancel> cancelBatchProcessing(String batchIdentity) {
        log.info("Started cancelling the batch id " + batchIdentity);
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.CANCEL_COSTING_BY_ID, Cancel.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity)
            .customBody("{}")
            .expectedResponseCode(HttpStatus.SC_ACCEPTED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Cancel any batch in a non-terminal state
     *
     * @param batch - object of Batch class
     */
    public static void checkAndCancelBatch(Batch batch) {
        List<String> batchState = new ArrayList<>();
        batchState.add("CANCELLED");
        batchState.add("ERRORED");
        batchState.add("REJECTED");
        batchState.add("COMPLETED");

        if (batchState.stream().anyMatch(state -> !state.contains(batch.getState()))) {
            BatchResources.cancelBatchProcessing(batch.getIdentity());
        }
    }

    /**
     * Checks an wait until the batch costing state reached to expected state
     *
     * @param batchIdentity    - Batch ID to send
     * @param bcsExpectedState - expected BCS State
     * @return BCSState
     */
    public static boolean waitUntilBatchCostingReachedExpected(String batchIdentity, BCSState bcsExpectedState) {
        long initialTime = System.currentTimeMillis() / 1000;
        RequestEntity requestEntity;
        Batch batch;
        do {
            requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, Batch.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, batchIdentity);
            batch = (Batch) HTTPRequest.build(requestEntity).get().getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(POLL_TIME);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!batch.getState().equals(bcsExpectedState.toString())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (batch.getState().equals(bcsExpectedState.toString())) ? true : false;
    }

    /**
     * Wait until batch costing is finished with one of status
     *
     * @param batchIdentity
     * @return boolean
     */
    @SneakyThrows
    public static boolean waitUntilBatchCostingReachedExpected(String batchIdentity) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(TIME_OUT);
        List<String> batchStatusList = Arrays.asList(new String[]{"COMPLETED", "ERRORED", "CANCELLED", "REJECTED"});
        Batch batch;
        batch = getBatchRepresentation(batchIdentity).getResponseEntity();
        while (!batchStatusList.stream().anyMatch(batch.getState()::contains)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            TimeUnit.SECONDS.sleep(POLL_TIME);
            batch = getBatchRepresentation(batchIdentity).getResponseEntity();
            log.debug(String.format("batch identity ID  >>%s<< ::: batch Status  >>%s<<", batch.getIdentity(), batch.getState()));
        }
        return true;

    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID.
     *
     * @param batchIdentity - Batch ID
     * @param klass         - Response class
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static <T> RequestEntity getBatchRequestEntity(BCSAPIEnum endPoint, String batchIdentity, Class<T> klass) {
        return RequestEntityUtil.init(endPoint, klass).inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity);
    }
}
