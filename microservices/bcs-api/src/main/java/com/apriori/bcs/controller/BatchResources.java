package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewBatchProperties;
import com.apriori.bcs.entity.request.NewBatchRequest;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Batches;
import com.apriori.bcs.entity.response.Cancel;
import com.apriori.bcs.entity.response.StartCosting;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import java.util.UUID;

public class BatchResources {

    public static <T> ResponseWrapper<T> getBatches() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCHES, Batches.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getBatchRepresentation(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_BY_ID, Batch.class)
            .inlineVariables(identity);

        return HTTPRequest.build(requestEntity).get();
    }

    public static Batch createNewBatch() {
        Long currentMillis = System.currentTimeMillis();

        NewBatchRequest body = new NewBatchRequest();
        NewBatchProperties newBatch = new NewBatchProperties();
        newBatch.setExternalId("Auto-Batch-" + currentMillis);
        newBatch.setRollupName("Auto-Rollup-" + currentMillis);
        newBatch.setRollupScenarioName("Auto-Scenario-" + currentMillis);
        newBatch.setExportSetName("Auto-ExportSet-" + System.currentTimeMillis());
        body.setBatch(newBatch);

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_BATCHES, Batch.class)
            .body(body);

        return (Batch) HTTPRequest.build(requestEntity)
            .post()
            .getResponseEntity();
    }

    public static StartCosting startCosting(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_START_COSTING_BY_ID, StartCosting.class)
            .inlineVariables(identity)
            .customBody("{}");

        return (StartCosting) HTTPRequest.build(requestEntity)
            .post()
            .getResponseEntity();

    }

    /**
     * Cancel the start-costing process
     *
     * @param <T> Object type
     * @return Cancel response
     */
    public static <T> ResponseWrapper<T> cancelBatchProccessing() {
        // create batch
        Batch batch = createNewBatch();
        String batchIdentity = batch.getIdentity();

        // create batch part
        NewPartRequest newPartRequest =
                (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);


        newPartRequest.setExternalId(UUID.randomUUID().toString());
        newPartRequest.setFilename("tab_forms.prt");
        BatchPartResources.createNewBatchPart(newPartRequest, batchIdentity);

        // start costing
        try {
            startCosting(batchIdentity);
        } catch (Exception ignored) {

        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_CANCEL_COSTING_BY_ID, Cancel.class)
            .inlineVariables(batchIdentity)
            .customBody("{}");

        return HTTPRequest.build(requestEntity).post();
    }

    public static <T> ResponseWrapper<T> cancelBatchProccessing(String batchIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_CANCEL_COSTING_BY_ID, Cancel.class)
            .inlineVariables(batchIdentity)
            .customBody("{}");

        return HTTPRequest.build(requestEntity).post();
    }
}
