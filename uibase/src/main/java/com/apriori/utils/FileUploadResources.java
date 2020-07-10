package com.apriori.utils;

import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.FileCommandEntity;
import com.apriori.apibase.services.response.objects.FileOrderEntity;
import com.apriori.apibase.services.response.objects.FileOrderResponseEntity;
import com.apriori.apibase.services.response.objects.FileOrdersEntity;
import com.apriori.apibase.services.response.objects.FileWorkOrderEntity;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.apibase.utils.APIAuthentication;
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

    APIAuthentication apiAuthentication = new APIAuthentication();

    public void initializeFileUpload(Object obj, String username) {
        NewPartRequest npr = (NewPartRequest) obj;
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        String token = apiAuthentication.getCachedToken(username);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        headers.put("Authorization", "Bearer " + token);
        headers.put("apriori.tenantgroup", "default");
        headers.put("apriori.tenant", "default");
        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(npr.getFilename())))
            .setFormParams(new FormParams().use("filename", npr.getFilename()));

        String res = GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getBody();

        JsonNode node;
        try {
            node = new ObjectMapper().readTree(res);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("can't read json node");
        }

        String id = node.findPath("identity").asText();

        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrderResponseEntity.class)
            .setHeaders(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/vnd.apriori.v1+json");
                    put("Authorization", "Bearer " + token);
                    put("apriori.tenantgroup", "default");
                    put("apriori.tenant", "default");
                }
            })
            .setBody(new FileCommandEntity()
                .setCommand(new FileOrdersEntity()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileOrderEntity().setScenarioName(npr.getScenarioName())
                        .setFileKey(id)
                        .setFileName(npr.getFilename()))));

        String orderId = GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody();

        JsonNode nodeId;
        try {
            nodeId = new ObjectMapper().readTree(orderId);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("can't read json node");
        }

        String orId = nodeId.findPath("id").asText();

        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                    put("Authorization", "Bearer " + token);
                    put("apriori.tenantgroup", "default");
                    put("apriori.tenant", "default");
                }
            })
            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList(orId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
    }
}
