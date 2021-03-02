package com.apriori.utils;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.entity.reponse.ComponentIdentityResponse;
import com.apriori.entity.reponse.GetComponentResponse;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.entity.reponse.componentiteration.ComponentIteration;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class CidAppTestUtil {

    private String url = Constants.getApiUrl();

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
        Constants.getCidServiceHost(),
        HttpStatus.SC_CREATED,
        Constants.getCidTokenUsername(),
        Constants.getCidTokenEmail(),
        Constants.getCidTokenIssuer(),
        Constants.getCidTokenSubject());

    /**
     * Adds a new component
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return responsewrapper
     */
    public ResponseWrapper<PostComponentResponse> postComponents(String scenarioName, String processGroup, String partName) {
        url = String.format(url, "components");

        RequestEntity requestEntity = RequestEntity.init(url, PostComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        url = String.format(url, "components");

        RequestEntity requestEntity = RequestEntity.init(url, GetComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching an identity
     *
     * @param identity - the identity
     * @return response object
     */
    public ResponseWrapper<ComponentIdentityResponse> getComponentIdentity(String identity) {
        url = String.format(url, String.format("components/%s", identity));

        RequestEntity requestEntity = RequestEntity.init(url, ComponentIdentityResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching an identity and component
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentScenarioIdentity(String componentIdentity, String scenarioIdentity) {
        url = String.format(url, String.format("components/%s/scenarios/%s/iterations/latest", componentIdentity ,scenarioIdentity ));

        RequestEntity requestEntity = RequestEntity.init(url, ComponentIteration.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }
}
