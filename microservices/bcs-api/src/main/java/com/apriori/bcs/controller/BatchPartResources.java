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
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

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

    /**
     * Gets a list of all parts associated to a specific batch
     *
     * @param batchIdentity The batch identity to retireve parts for
     * @param returnType The parts class object
     * @param customer Customer identity
     * @param <T> The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getBatchParts(String batchIdentity, Class returnType, String customer) {
        if (!batchIdentity.isEmpty()) {
            batchIdentity = batchIdentity + "/";
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PARTS, returnType)
                .inlineVariables(batchIdentity);

        if (customer != null) {
            return HTTPRequest.build(requestEntity).getWithCustomer(customer);
        } else {
            return HTTPRequest.build(requestEntity).get();
        }
    }

    public static <T> ResponseWrapper<T> getBatchParts(String batchIdentity, Class returnType) {
        return getBatchParts(batchIdentity, returnType, null);
    }

    public static <T> ResponseWrapper<T> getBatchParts(String batchIdentity) {
        return getBatchParts(batchIdentity, Parts.class);
    }

    /**
     * Gets a list of all parts associated to a specific batch
     *
     * @param batchIdentity The batch identity to retrieve parts for
     * @param partIdentity The identity of the part to retrieve
     * @param returnType The parts class object
     * @param customer Customer identity
     * @param <T> The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity,
                                                                    Class returnType, String customer) {
        if (!batchIdentity.isEmpty()) {
            batchIdentity = batchIdentity + "/";
        }

        if (!partIdentity.isEmpty()) {
            partIdentity = partIdentity + "/";
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PART_BY_BATCH_PART_IDS, returnType)
            .inlineVariables(batchIdentity, partIdentity);

        if (customer != null) {
            return HTTPRequest.build(requestEntity).getWithCustomer(customer);
        } else {
            return HTTPRequest.build(requestEntity).get();
        }
    }

    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity,
                                                                    Class returnType) {
        return getBatchPartRepresentation(batchIdentity, partIdentity, returnType, null);
    }

    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity) {

        return getBatchPartRepresentation(batchIdentity, partIdentity, Part.class);
    }


    /**
     * Create a part with no material
     *
     * @param npr
     * @param batchIdentity
     * @param <T>
     * @return
     */
    public static <T> ResponseWrapper<T> createNewBatchPartNoMaterial(NewPartRequest npr, String batchIdentity) {
        npr.setMaterialName(null);
        return createNewBatchPart(npr, batchIdentity);
    }

    public static <T> ResponseWrapper<T> createNewBatchPart(NewPartRequest npr, String batchIdentity,
                                                            ProcessGroupValue processGroupValue) {
        return createNewBatchPart(npr, batchIdentity, processGroupValue, Part.class, null);
    }

    /**
     * Create a new part
     *
     * @param npr New part request
     * @param batchIdentity Batch identity
     * @param processGroupValue Process group to use
     * @param returnType The object class to return. If null, then the default
     *                   response schema validation is bypassed
     * @param customer The customer identity
     * @param <T>
     * @return
     */
    public static <T> ResponseWrapper<T> createNewBatchPart(NewPartRequest npr, String batchIdentity,
                                                            ProcessGroupValue processGroupValue, Class returnType,
                                                            String customer) {
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

        if (!batchIdentity.isEmpty()) {
            batchIdentity = batchIdentity + "/";
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_BATCH_PARTS_BY_ID, returnType)
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
                        //.use("PinnedRouting", npr.getPinnedRouting())
                        .use("ProcessGroup", processGroup)
                        .use("ProductionLife", npr.getProductionLife().toString())
                        .use("ScenarioName", npr.getScenarioName() + System.currentTimeMillis())
                        //.use("Udas", npr.getUdas())
                        .use("VpeName", npr.getVpeName())
                        .use("MaterialName", npr.getMaterialName())
                        .use("generateWatchpointReport", "true")
                );

        if (customer == null) {
            return HTTPRequest.build(requestEntity).postMultipart();
        } else {
            return HTTPRequest.build(requestEntity).postMultipartWithCustomer(customer);
        }
    }

    public static <T> ResponseWrapper<T> createNewBatchPart(NewPartRequest npr, String batchIdentity) {
        return createNewBatchPart(npr, batchIdentity, ProcessGroupValue.USE_PROCESS_GROUP);
    }


    public static <T> ResponseWrapper<T> getResultsNoPoll(String batchIdentity, String partIdentity,
                                                    Class returnType, String customer) {
        return  getResults(batchIdentity, partIdentity, returnType, customer, false);

    }

    /**
     * Gets costing results a specific batch & part
     *
     * @param batchIdentity The batch identity to retrieve parts for
     * @param partIdentity The identity of the part to retrieve
     * @param returnType The parts class object
     * @param customer Customer identity
     * @param doPoll True will trigger polling the part state
     * @param <T> The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity,
                                                    Class returnType, String customer, Boolean doPoll) {
        if (doPoll) {
            Object partDetails;
            BcsUtils.State isPartComplete = BcsUtils.State.PROCESSING;
            int count = 0;
            while (count <= Constants.BATCH_POLLING_TIMEOUT * 2) {
                partDetails =
                        BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
                isPartComplete = BcsUtils.pollState(partDetails, Part.class);

                if (isPartComplete.equals(BcsUtils.State.COMPLETED)) {
                    break;
                }
                count += 1;
            }
        }

        if (!batchIdentity.isEmpty()) {
            batchIdentity = batchIdentity + "/";
        }

        if (!partIdentity.isEmpty()) {
            partIdentity = partIdentity + "/";
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_RESULTS_BY_BATCH_PART_IDS, returnType)
                .inlineVariables(batchIdentity, partIdentity);

        if (customer != null) {
            return HTTPRequest.build(requestEntity).getWithCustomer(customer);
        } else {
            return HTTPRequest.build(requestEntity).get();
        }
    }

    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity,
                                                    Class returnType) {
        return getResults(batchIdentity, partIdentity, returnType, null, true);
    }

    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity) {
        return getResults(batchIdentity, partIdentity, Results.class);
    }

    public static <T> ResponseWrapper<T> getResults(String batchIdentity, String partIdentity,
                                                    Class returnType, String customer) {
        return getResults(batchIdentity, partIdentity, returnType, customer, true);
    }


    public static <T> ResponseWrapper<T> getPartReportNoPoll(String batchIdentity, String partIdentity,
                                                       Class returnType, String customer) {
        return getPartReport(batchIdentity, partIdentity, returnType, customer, false);
    }

    /**
     * Get report a specific batch & part
     *
     * @param batchIdentity The batch identity to retrieve parts for
     * @param partIdentity The identity of the part to retrieve
     * @param returnType The parts class object
     * @param customer Customer identity
     * @param <T> The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity,
                                                       Class returnType, String customer, Boolean doPoll) {

        if (doPoll) {
            Object partDetails;
            BcsUtils.State isPartComplete = BcsUtils.State.PROCESSING;
            int count = 0;
            while (count <= Constants.BATCH_POLLING_TIMEOUT) {
                partDetails =
                        BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
                isPartComplete = BcsUtils.pollState(partDetails, Part.class);

                if (isPartComplete.equals(BcsUtils.State.COMPLETED)) {
                    break;
                }
                count += 1;
            }
        }

        if (!batchIdentity.isEmpty()) {
            batchIdentity = batchIdentity + "/";
        }

        if (!partIdentity.isEmpty()) {
            partIdentity = partIdentity + "/";
        }

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_PART_REPORT_BY_BATCH_PART_IDS, returnType)
                .inlineVariables(batchIdentity, partIdentity);

        if (customer != null) {
            return HTTPRequest.build(requestEntity).getWithCustomer(customer);
        } else {
            return HTTPRequest.build(requestEntity).get();
        }
    }

    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity,
                                                    Class returnType) {
        return getPartReport(batchIdentity, partIdentity, returnType, null, true);
    }

    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity) {
        return getPartReport(batchIdentity, partIdentity, PartReport.class, null, true);
    }


    /**
     * Generate a newpartrequest
     *
     * @return newPartRequest
     */
    public static NewPartRequest getNewPartRequest() {
        NewPartRequest newPartRequest =
                (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");

        return newPartRequest;
    }

}
