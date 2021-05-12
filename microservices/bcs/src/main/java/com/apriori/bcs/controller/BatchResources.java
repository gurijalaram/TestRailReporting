package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewBatchProperties;
import com.apriori.bcs.entity.request.NewBatchRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Batches;
import com.apriori.bcs.entity.response.StartCosting;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class BatchResources extends CisBase {
    private static final String endpointBatches = String.format(getBatchUrl(), "");
    private static final String endpointBatchesWithIdentity = String.format(getBatchUrlWithIdentity(), "");
    private static final String endPointBatchCosting = String.format(getBatchUrlWithIdentity(), "/start-costing");

    public static <T> ResponseWrapper<T> getBatches() {
        String url = endpointBatches;
        return GenericRequestUtil.get(
                RequestEntity.init(url, Batches.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getBatchRepresentation(String identity) {
        String url = String.format(getBatchUrl(), "/" + identity);;
        return GenericRequestUtil.get(
                RequestEntity.init(url, Batch.class),
                new RequestAreaApi()
        );
    }

    public static Batch createNewBatch() {
        String url = endpointBatches;
        Long currentMillis = System.currentTimeMillis();

        NewBatchRequest body = new NewBatchRequest();
        NewBatchProperties newBatch = new NewBatchProperties();
        newBatch.setExternalId("Auto-Batch-" + currentMillis);
        newBatch.setRollupName("Auto-Rollup-" + currentMillis);
        newBatch.setRollupScenarioName("Auto-Scenario-" + currentMillis);
        newBatch.setExportSetName("Auto-ExportSet-" + System.currentTimeMillis());
        body.setBatch(newBatch);

        return (Batch) GenericRequestUtil.post(
                RequestEntity.init(url, Batch.class)
                        .setBody(body)
                        .setStatusCode(HttpStatus.SC_CREATED),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static StartCosting startCosting(String identity) {
        String url = String.format(getBatchUrlWithIdentity(identity), "/start-costing");

        return (StartCosting) GenericRequestUtil.post(
                RequestEntity.init(url, StartCosting.class)
                        .setBody("{}")
                        .setStatusCode(HttpStatus.SC_ACCEPTED),
                new RequestAreaApi()
        ).getResponseEntity();
    }
}
