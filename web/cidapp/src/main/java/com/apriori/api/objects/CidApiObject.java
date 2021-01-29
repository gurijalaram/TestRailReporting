package com.apriori.api.objects;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JsonNodeUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class CidApiObject {

    private static String componentIdentity;
    private static String scenarioIdentity;
    String CONTENT_TYPE = "Content-Type";
    Map<String, String> headers = new HashMap<>();
    ;

    public ResponseWrapper<Object> uploadFile(String token, String apiUrl, Class klass, String scenarioName, String partName) {
        headers.put(CONTENT_TYPE, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(apiUrl, klass)
            .setHeaders(headers)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        ResponseWrapper<Object> request = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        componentIdentity = new JsonNodeUtil().jsonNode(request.getBody(), "componentIdentity");
        scenarioIdentity = new JsonNodeUtil().jsonNode(request.getBody(), "scenarioIdentity");
        return request;
    }

}
