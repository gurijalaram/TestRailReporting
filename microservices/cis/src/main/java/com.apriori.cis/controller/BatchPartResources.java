package com.apriori.cis.controller;

import com.apriori.cis.entity.request.NewPartRequest;
import com.apriori.cis.entity.response.Part;
import com.apriori.cis.entity.response.Parts;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class BatchPartResources extends CisBase {

    private static final String endpointBatchPart = String.format(getBatchUrlWithIdentity(), "/parts");
    private static final String endpointBatchPartWithIdentity = String.format(getBatchUrlWithIdentity(),
            String.format("/parts/%s", CommonConstants.getCisPartIdentity()));

    public static <T> ResponseWrapper<T> getBatchParts() {
        return GenericRequestUtil.get(
            RequestEntity.init(endpointBatchPart, Parts.class),
            new RequestAreaApi()
        );
    }


    public static <T> ResponseWrapper<T> getBatchPartRepresentation() {
        return GenericRequestUtil.get(
                RequestEntity.init(endpointBatchPartWithIdentity, Part.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity) {
        String url = String.format(getBatchUrlWithIdentity(batchIdentity),
                String.format("/parts/%s", partIdentity));
        return GenericRequestUtil.get(
                RequestEntity.init(url, Part.class),
                new RequestAreaApi()
        );
    }

    public static Part createNewBatchPart(NewPartRequest npr) {
        return createNewBatchPart(npr, null);
    }

    public static Part createNewBatchPart(NewPartRequest npr, String identity) {
        String url;

        if (identity != null) {
            url = getBatchUrlWithIdentity(identity);
            url = String.format(url, "/parts");
        } else {
            url = endpointBatchPart;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        RequestEntity requestEntity = RequestEntity.init(url, Part.class)
                .setHeaders(headers)
                .setMultiPartFiles(new MultiPartFiles()
                        .use("data", FileResourceUtil.getResourceAsFile(npr.getFilename()))
                )
                .setFormParams(new FormParams()
                        .use("filename", npr.getFilename())
                        .use("externalId", String.format(npr.getExternalId(), System.currentTimeMillis()))
                        .use("AnnualVolume", npr.getAnnualVolume().toString())
                        .use("BatchSize", npr.getBatchSize().toString())
                        .use("Description", npr.getDescription())
                        .use("PinnedRouting", npr.getPinnedRouting())
                        .use("ProcessGroup", npr.getProcessGroup())
                        .use("ProductionLife", npr.getProductionLife().toString())
                        .use("ScenarioName", npr.getScenarioName())
                        .use("Udas", npr.getUdas())
                        .use("VpeName", npr.getVpeName())
                        .use("MaterialName", npr.getMaterialName()));


        return (Part)GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }
}
