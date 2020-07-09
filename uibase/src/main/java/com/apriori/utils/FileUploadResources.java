package com.apriori.utils;

import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.FileOrdersEntity;
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

import java.util.HashMap;
import java.util.Map;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);

    APIAuthentication apiAuthentication = new APIAuthentication();
    private String id;

    public void initializeFileUpload(Object obj, String username) {
        NewPartRequest npr = (NewPartRequest) obj;
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        headers.put("Authorization", "Bearer " + apiAuthentication.getCachedToken(username));
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

        id = node.findPath("identity").asText();

        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrdersEntity.class)
            .setFormParams(new FormParams().use("commandType", "LOADCADFILE")
                .use("scenarioName", "CFInitial")
                .use("fileKey", id)
                .use("fileName", npr.getFilename()));

        GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getResponseEntity();
    }
}
