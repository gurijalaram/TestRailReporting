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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains methods Utils related Batch-Controller APIs.
 * <li>Get Batches
 * <li>Create Batch
 * <li>Get Batch with Batch ID
 * <li>Cancel Batch
 * <li>Start-Costing
 */
public class BatchResources {
    private static final Logger logger = LoggerFactory.getLogger(BatchResources.class);

    /**
     * Creates Batch
     *
     * @return Batch response instance
     */
    public static <T> ResponseWrapper<T> createBatch() {
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
     * @param <T> - Response of Type Object
     * @return Response Object
     */
    public static <T> ResponseWrapper<T> getBatches() {
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
    public static <T> ResponseWrapper<T> getBatchRepresentation(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, Batch.class)
                .inlineVariables(identity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Costing a batch
     *
     * @param batch - Batch ID
     * @param <T>   - Response of type Object
     * @return - response
     */
    public static <T> ResponseWrapper<T> startBatchCosting(Batch batch) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.START_COSTING_BY_ID, null)
                .inlineVariables(batch.getIdentity())
                .customBody("{}");
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Cancel the batch
     *
     * @param batchIdentity - Batch ID
     * @param <T>           - Response of type Object
     * @return - response
     */
    public static <T> ResponseWrapper<T> cancelBatchProcessing(String batchIdentity) {
        logger.info("Started cancelling the batch id " + batchIdentity);
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.CANCEL_COSTING_BY_ID, Cancel.class)
                .inlineVariables(batchIdentity)
                .customBody("{}");
        return HTTPRequest.build(requestEntity).post();
    }
}
