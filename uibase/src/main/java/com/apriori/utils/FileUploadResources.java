package com.apriori.utils;

import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.FileCommandEntity;
import com.apriori.apibase.services.response.objects.FileOrderEntity;
import com.apriori.apibase.services.response.objects.FileOrderResponse;
import com.apriori.apibase.services.response.objects.FileOrdersEntity;
import com.apriori.apibase.services.response.objects.FileWorkOrderEntity;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.apibase.services.response.objects.WorkOrder;
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

    public void createFileUpload(HashMap<String, String> token, Object fileObject) {
        initializeFileUpload(token, fileObject);
        createFileUploadWorkOrder(token, fileObject);
        submitFileUploadWorkOrder(token);
        checkFileStatus(token);
    }

    private void initializeFileUpload(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(npr.getFilename())))
            .setFormParams(new FormParams().use("filename", npr.getFilename()));

        String fileBody = GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getBody();

        identity = jsonNode(fileBody, "identity");
    }

    private void createFileUploadWorkOrder(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/vnd.apriori.v1+json");

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileCommandEntity()
                .setCommand(new FileOrdersEntity()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileOrderEntity().setScenarioName(npr.getScenarioName())
                        .setFileKey(identity)
                        .setFileName(npr.getFilename()))));

        String fileBody = GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody();

        orderId = jsonNode(fileBody, "id");
    }

    private void submitFileUploadWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList(orderId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
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
