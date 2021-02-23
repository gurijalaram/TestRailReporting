package com.apriori;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.entity.reponse.GetComponentResponse;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.utils.Constants;
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
     * Adds a new component
     *
     * @param token        - the token
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return responsewrapper
     */
    public ResponseWrapper<PostComponentResponse> postComponents(String token, String scenarioName, String partName) {
        final String url = String.format(Constants.getApiUrl(), "components");

        RequestEntity requestEntity = RequestEntity.init(url, PostComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        ResponseWrapper<PostComponentResponse> response = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        componentIdentity = response.getResponseEntity().getComponentIdentity();
        scenarioIdentity = response.getResponseEntity().getScenarioIdentity();
        return response;
    }

    /**
     * Find components for the current user matching a specified query.
     * @param token - the token
     * @return responsewrapper
     */
    public ResponseWrapper<GetComponentResponse> getComponents(String token) {
        final String url = String.format(Constants.getApiUrl(), "components");

        RequestEntity requestEntity = RequestEntity.init(url, GetComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }
}
