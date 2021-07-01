package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BatchPartResources extends BcsBase {

    enum EndPoint {
        BATCH_PARTS(String.format(getCisUrl(),"batches/%s/parts")),
        GET_BATCH_PART_REPRESENTATION(String.format(getCisUrl(), "batches/%s/parts/%s")),
        GET_RESULTS(String.format(getCisUrl(), "batches/%s/parts/%s/results")),
        GET_PART_REPORT(String.format(getCisUrl(), "batches/%s/parts/%s/part-report"));

        private final String endPoint;
        EndPoint(String ep) {
            endPoint = ep;
        }

        String getEndPoint() {
            return endPoint;
        }
    }

    public static <T> ResponseWrapper<T> getBatchParts(String batchIdentity) {
        return GenericRequestUtil.get(
            RequestEntity.init(String.format(EndPoint.BATCH_PARTS.getEndPoint(), batchIdentity),
                    Parts.class),
            new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity) {
        String url = String.format(EndPoint.GET_BATCH_PART_REPRESENTATION.getEndPoint(), batchIdentity, partIdentity);
        return GenericRequestUtil.get(
                RequestEntity.init(url, Part.class),
                new RequestAreaApi()
        );
    }

    public static Part createNewBatchPart(NewPartRequest npr, String batchIdentity) {
        String url = String.format(EndPoint.BATCH_PARTS.getEndPoint(),
                batchIdentity);

        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(npr.getProcessGroup()),
                npr.getFilename());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        RequestEntity requestEntity = RequestEntity.init(url, Part.class)
                .setHeaders(headers)
                .setMultiPartFiles(new MultiPartFiles()
                 .use("data", partFile)
                )
                 .setFormParams(new FormParams()
                        .use("filename", npr.getFilename())
                        .use("externalId", String.format(npr.getExternalId(), System.currentTimeMillis()))
                        .use("AnnualVolume", npr.getAnnualVolume().toString())
                        .use("BatchSize", npr.getBatchSize().toString())
                        .use("Description", npr.getDescription())
                        //.use("PinnedRouting", npr.getPinnedRouting())
                        .use("ProcessGroup", npr.getProcessGroup())
                        //.use("ProductionLife", npr.getProductionLife().toString())
                        .use("ScenarioName", npr.getScenarioName() + System.currentTimeMillis())
                        //.use("Udas", npr.getUdas())
                        //.use("VpeName", npr.getVpeName())
                        .use("MaterialName", npr.getMaterialName())
                        .use("generateWatchpointReport", "true")
                );


        return (Part)GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }


    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity) {
        Object partDetails;
        BcsUtils.State isPartComplete;
        int count = 0;
        while (count <= Constants.getPollingTimeout()) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            isPartComplete = BcsUtils.pollState(partDetails, Part.class);
            if (isPartComplete.equals(BcsUtils.State.COMPLETE)) {
                break;
            }
            count += 1;
        }

        String url = String.format(EndPoint.GET_RESULTS.getEndPoint(), batchIdentity, partIdentity);
        return GenericRequestUtil.get(
                RequestEntity.init(url, Results.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity) {
        Object partDetails;
        BcsUtils.State isPartComplete;
        int count = 0;
        while (count <= Constants.getPollingTimeout()) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            isPartComplete = BcsUtils.pollState(partDetails, Part.class);
            if (isPartComplete.equals(BcsUtils.State.COMPLETE)) {
                break;
            }
            count += 1;
        }

        String url = String.format(EndPoint.GET_PART_REPORT.getEndPoint(), batchIdentity, partIdentity);
        return GenericRequestUtil.get(
                RequestEntity.init(url, PartReport.class),
                new RequestAreaApi()
        );
    }
}
