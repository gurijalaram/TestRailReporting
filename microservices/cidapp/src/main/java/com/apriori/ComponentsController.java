package com.apriori;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JsonNodeUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

public class ComponentsController {

    private static String componentIdentity;
    private static String scenarioIdentity;

    /**
     * Post a component
     *
     * @param token        - the token
     * @param url          - the url
     * @param klass        - the response class
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return object
     */
    public ResponseWrapper<Object> postComponents(String token, String url, Class klass, String scenarioName, String partName) {

        RequestEntity requestEntity = RequestEntity.init(url, klass)
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
