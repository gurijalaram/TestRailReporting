package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// TODO ALL: test it
public class BatchPartResources {

    public enum ProcessGroupValue {
        USE_NULL,
        USE_EMPTY_STRING,
        USE_PROCESS_GROUP
    }

    public static <T> ResponseWrapper<T> getBatchParts(String batchIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PARTS_BY_ID, Parts.class)
            .inlineVariables(batchIdentity);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PART_REPRESENTATION_BY_BATCH_PART_IDS, Part.class)
            .inlineVariables(batchIdentity, partIdentity);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> createNewBatchPart(NewPartRequest npr, String batchIdentity,
                                                            ProcessGroupValue processGroupValue) {
        String processGroup;
        switch (processGroupValue) {
            case USE_PROCESS_GROUP:
            default:
                processGroup = npr.getProcessGroup();
                break;
            case USE_NULL:
                processGroup = null;
                break;
            case USE_EMPTY_STRING:
                processGroup = "";
                break;

        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");

        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(npr.getProcessGroup()),
            npr.getFilename());

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_BATCH_PARTS_BY_ID, Part.class)
            .inlineVariables(batchIdentity)
            .headers(headers)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partFile)
            )
            .formParams(new FormParams()
                .use("filename", npr.getFilename())
                .use("externalId", String.format(npr.getExternalId(), System.currentTimeMillis()))
                .use("AnnualVolume", npr.getAnnualVolume().toString())
                .use("BatchSize", npr.getBatchSize().toString())
                .use("Description", npr.getDescription())
                .use("ProcessGroup", processGroup)
                .use("ScenarioName", npr.getScenarioName() + System.currentTimeMillis())
                .use("VpeName", npr.getVpeName())
                .use("MaterialName", npr.getMaterialName())
                .use("generateWatchpointReport", "true")
            );

        return HTTP2Request.build(requestEntity).postMultipart();
    }

    public static <T> ResponseWrapper<T> createNewBatchPart(NewPartRequest npr, String batchIdentity) {
        return createNewBatchPart(npr, batchIdentity, ProcessGroupValue.USE_PROCESS_GROUP);
    }


    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity) {
        Object partDetails;
        BcsUtils.State isPartComplete = BcsUtils.State.PROCESSING;
        int count = 0;
        while (count <= Constants.POLLING_TIMEOUT * 2) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            try {
                isPartComplete = BcsUtils.pollState(partDetails, Part.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPartComplete.equals(BcsUtils.State.COMPLETED)) {
                break;
            }
            count += 1;
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_RESULTS_BY_BATCH_PART_IDS, Results.class)
            .inlineVariables(batchIdentity, partIdentity);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity) {
        Object partDetails;
        BcsUtils.State isPartComplete = BcsUtils.State.PROCESSING;
        int count = 0;
        while (count <= Constants.POLLING_TIMEOUT) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            try {
                isPartComplete = BcsUtils.pollState(partDetails, Part.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPartComplete.equals(BcsUtils.State.COMPLETED)) {
                break;
            }
            count += 1;
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_PART_REPORT_BY_BATCH_PART_IDS, PartReport.class)
            .inlineVariables(batchIdentity, partIdentity);

        return HTTP2Request.build(requestEntity).get();
    }
}
