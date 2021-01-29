package com.apriori.api.objects;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
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

    String CONTENT_TYPE = "Content-Type";
    Map<String, String> headers = new HashMap<>();
    String token;

    public CidApiObject getToken(String secretKey, String serviceHost, int httpStatus, String tokenUsername, String tokenEmail, String tokenIssuer, String tokenSubject) {
        token = new JwtTokenUtil().retrieveJwtToken(secretKey, serviceHost, httpStatus, tokenUsername, tokenEmail, tokenIssuer, tokenSubject);
        return this;
    }

    public ResponseWrapper<Object> uploadFile(String apiUrl, Class klass, String scenarioName, String partName) {
        headers.put(CONTENT_TYPE, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(apiUrl, klass)
            .setHeaders(headers)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}
