package com.apriori;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.entity.reponse.ComponentIdentityResponse;
import com.apriori.entity.reponse.GetComponentResponse;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

public class CidAppTestUtil {

    /**
     * Adds a new component
     *
     * @param url          - the url
     * @param token        - the token
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return responsewrapper
     */
    public ResponseWrapper<PostComponentResponse> postComponents(String url, String token, String scenarioName, String partName) {
        RequestEntity requestEntity = RequestEntity.init(url, PostComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @param url   - the url
     * @param token - the token
     * @return responsewrapper
     */
    public ResponseWrapper<GetComponentResponse> getComponents(String url, String token) {
        RequestEntity requestEntity = RequestEntity.init(url, GetComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    /**
     * Get the current representation of a component.
     *
     * @param url   - the url
     * @param token - the token
     * @return component identity
     */
    public ResponseWrapper<ComponentIdentityResponse> getComponentsIdentity(String url, String token) {
        RequestEntity requestEntity = RequestEntity.init(url, ComponentIdentityResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }
}
