package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.batch.BatchProperties;
import com.apriori.bcs.entity.request.batch.BatchRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Batches;
import com.apriori.bcs.entity.response.Cancel;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains methods Utils related Batch-Controller APIs.
 * <li>Get Batches
 * <li>Create Batch
 * <li>Get Batch with Batch ID
 * <li>Cancel Batch
 * <li>Start-Costing
 */
@Slf4j
public class BatchResources {

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
            .headers(headerInfo)
            .body(newBatchRequest);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Returns list of batches
     *
     * @return Response Object
     */
    public static ResponseWrapper<Batches> getBatches() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCHES, Batches.class);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get single batch with batch id
     *
     * @param identity - Batch Id
     * @param <T>      - response of type Object
     * @return - response
     */
    public static ResponseWrapper<Batch> getBatchRepresentation(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, Batch.class)
            .inlineVariables(identity);
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
            .inlineVariables(batch.getIdentity())
            .customBody("{}");
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
            .inlineVariables(batchIdentity)
            .customBody("{}");
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
}
