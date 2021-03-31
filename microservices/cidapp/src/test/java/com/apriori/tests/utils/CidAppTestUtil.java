package com.apriori.tests.utils;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.entity.request.CostRequest;
import com.apriori.entity.response.ComponentIdentityResponse;
import com.apriori.entity.response.GetComponentResponse;
import com.apriori.entity.response.PostComponentResponse;
import com.apriori.entity.response.componentiteration.ComponentIteration;
import com.apriori.entity.response.scenarios.CostResponse;
import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CidAppTestUtil {
    private static final Logger logger = LoggerFactory.getLogger(CidAppTestUtil.class);

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
        Constants.getCidServiceHost(),
        HttpStatus.SC_CREATED,
        Constants.getCidTokenUsername(),
        Constants.getCidTokenEmail(),
        Constants.getCidTokenIssuer(),
        Constants.getCidTokenSubject());

    private String url;
    private String serviceUrl = Constants.getApiUrl();

    /**
     * Adds a new component
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return responsewrapper
     */
    public ResponseWrapper<PostComponentResponse> postComponents(String scenarioName, String processGroup, String partName) {
        url = String.format(serviceUrl, "components");

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
        url = String.format(serviceUrl, "components");

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
        url = String.format(serviceUrl, String.format("components/%s", identity));

        RequestEntity requestEntity = RequestEntity.init(url, ComponentIdentityResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching an identity and component
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(String componentIdentity, String scenarioIdentity) {
        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s/iterations/latest", componentIdentity, scenarioIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, ComponentIteration.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }

    /**
     * Gets the scenario representation
     *
     * @param transientState    - the impermanent state
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<CostResponse> getScenarioRepresentation(String transientState, String componentIdentity, String scenarioIdentity) {
        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s", componentIdentity, scenarioIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, CostResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        long startTime = System.currentTimeMillis() / 1000;
        String scenarioState;
        ResponseWrapper<CostResponse> scenarioRepresentation;

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        do {
            scenarioRepresentation = GenericRequestUtil.get(requestEntity, new RequestAreaApi());
            scenarioState = scenarioRepresentation.getResponseEntity().getResponse().getScenarioState();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while ((scenarioState.equals(transientState.toUpperCase()) && ((System.currentTimeMillis() / 1000) - startTime) < 180));
        return scenarioRepresentation;
    }

    /**
     * Post to cost a component
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<CostResponse> postCostComponent(String componentIdentity, String scenarioIdentity) {
        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s/cost", componentIdentity, scenarioIdentity));

        RequestEntity requestEntity = RequestEntity.init(url, CostResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("costingInputs",
                new CostRequest().setAnnualVolume(5500)
                    .setBatchSize(458)
                    .setMaterialName("Aluminum, Stock, ANSI 1050A")
                    .setProcessGroupName("Sheet Metal")
                    .setProductionLife(5.0)
                    .setVpeName("aPriori USA"));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

}
