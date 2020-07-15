package com.apriori.utils;

import com.apriori.apibase.services.cid.objects.cost.FileOrderResponse;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.MaterialBean;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfo;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ScenarioKey;
import com.apriori.apibase.services.cid.objects.upload.FileCommandEntity;
import com.apriori.apibase.services.cid.objects.upload.FileOrderEntity;
import com.apriori.apibase.services.cid.objects.upload.FileOrdersEntity;
import com.apriori.apibase.services.cid.objects.upload.FileUploadWorkOrder;
import com.apriori.apibase.services.cid.objects.upload.FileWorkOrderEntity;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);

    private static String identity;
    private static String orderId;
    private static String stateName;
    private static int workspaceId;
    private static String typeName;
    private static String masterName;
    private static String iteration;
    Map<String, String> headers = new HashMap<>();

    private String contentType = "Content-Type";
    private String applicationJson = "application/json";

    public void createFileUpload(HashMap<String, String> token, Object fileObject) {
        initializeFileUpload(token, fileObject);
        createFileUploadWorkOrder(token, fileObject);
        submitFileUploadWorkOrder(token);
        checkFileWorkOrderStatus(token);
        initializeCostScenario(token);
    }

    private void initializeFileUpload(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        headers.put(contentType, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(npr.getFilename())))
            .setFormParams(new FormParams().use("filename", npr.getFilename()));

        identity = jsonNode(GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getBody(), "identity");
    }

    private void createFileUploadWorkOrder(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileCommandEntity()
                .setCommand(new FileOrdersEntity()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileOrderEntity().setScenarioName(npr.getScenarioName())
                        .setFileKey(identity)
                        .setFileName(npr.getFilename()))));

        orderId = jsonNode(GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    private void submitFileUploadWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList(orderId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
    }

    private void checkFileWorkOrderStatus(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/" + orderId;

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileUploadWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token);

        long startTime = System.currentTimeMillis() / 1000;

        do {
            jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status");
        } while ((!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("SUCCESS")) &&
            (!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("FAILED")) &&
            ((System.currentTimeMillis() / 1000) - startTime) < 30);

        String orderBody = GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody();

        workspaceId = Integer.parseInt(jsonNode(orderBody, "workspaceId"));
        typeName = jsonNode(orderBody, "typeName");
        masterName = jsonNode(orderBody, "masterName");
        stateName = jsonNode(orderBody, "stateName");
        iteration = jsonNode(orderBody, "iteration");
    }

    private void initializeCostScenario(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + masterName + "/" + stateName + "/iterations/" + iteration + "/production-info";

        headers.put(contentType, applicationJson);

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new ProductionInfo().setScenarioKey(
                new ScenarioKey().setTypeName(typeName)
                    .setStateName(stateName)
                    .setWorkspaceId(workspaceId)
                    .setMasterName(masterName))
                .setProcessGroupName("Sheet Metal")
                .setMaterialBean(new MaterialBean().setVpeDefaultMaterialName("Steel, Cold Worked, AISI 1012")));

        GenericRequestUtil.post(costRequestEntity, new RequestAreaApi()).getBody();
    }

    private String jsonNode(String jsonProperties, String path) {
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(jsonProperties);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("can't read json node");
        }
        return node.findPath(path).asText();
    }
}
