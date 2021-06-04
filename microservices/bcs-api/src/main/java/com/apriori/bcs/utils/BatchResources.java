package com.apriori.bcs.utils;

import com.apriori.apibase.services.bcs.objects.Batch;
import com.apriori.apibase.services.bcs.objects.Batches;
import com.apriori.apibase.services.bcs.objects.StartCosting;
import com.apriori.apibase.services.bcs.objects.requests.NewBatchProperties;
import com.apriori.apibase.services.bcs.objects.requests.NewBatchRequest;
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
        NewBatchRequest body = new NewBatchRequest();
        NewBatchProperties newBatch = new NewBatchProperties();
        newBatch.setExternalId("Auto-Batch-" + System.currentTimeMillis());
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
