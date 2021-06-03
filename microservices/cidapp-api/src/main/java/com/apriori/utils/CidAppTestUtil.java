package com.apriori.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidapp.entity.enums.CidAppAPIEnum;
import com.apriori.cidapp.entity.request.CostRequest;
import com.apriori.cidapp.entity.response.ComponentIdentityResponse;
import com.apriori.cidapp.entity.response.GetComponentResponse;
import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.cidapp.entity.response.componentiteration.ComponentIteration;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.cidapp.entity.response.scenarios.CostResponse;
import com.apriori.cidapp.entity.response.scenarios.ImageResponse;
import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.users.UserCredentials;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CidAppTestUtil {
    private static final Logger logger = LoggerFactory.getLogger(CidAppTestUtil.class);

    private String token;

    /**
     * Adds a new component
     *
     * @param componentName   - the part name
     * @param scenarioName    - the scenario name
     * @param resourceFile    - the process group
     * @param userCredentials - the user credentials
     * @return response object
     */
    public Item postComponents(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {

        token = userCredentials == null ? new JwtTokenUtil().retrieveJwtToken() : new JwtTokenUtil(userCredentials).retrieveJwtToken();

        RequestEntityUtil.useTokenForRequests(token);
        return postComponents(componentName, scenarioName, resourceFile);
    }


    /**
     * Adds a new component
     *
     * @param componentName - the part name
     * @param scenarioName  - the scenario name
     * @return responsewrapper
     */
    public Item postComponents(String componentName, String scenarioName, String resourceFile) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(resourceFile), componentName)))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName));

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        ResponseWrapper<CssComponentResponse> itemResponse = new UncostedComponents().getUnCostedCssComponents(componentName, scenarioName);

        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, itemResponse.getStatusCode());
        return itemResponse.getResponseEntity().getItems().get(0);
    }

    /**
     * Adds a new component
     *
     * @param scenarioName  - the scenario name
     * @param componentName - the part name
     * @return responsewrapper
     */
    public Item postComponents(String componentName, String scenarioName, File resourceFile) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", resourceFile))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName));

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        ResponseWrapper<CssComponentResponse> itemResponse = new UncostedComponents().getUnCostedCssComponents(componentName, scenarioName);

        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, itemResponse.getStatusCode());
        return itemResponse.getResponseEntity().getItems().get(0);
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENTS, GetComponentResponse.class);

        return HTTP2Request.build(requestEntity).get();
    }

    /**
     * Find components for the current user matching an identity
     *
     * @param componentIdentity - the identity
     * @return response object
     */
    public ResponseWrapper<ComponentIdentityResponse> getComponentIdentity(String componentIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENT_BY_COMPONENT_ID, ComponentIdentityResponse.class)
                .inlineVariables(Collections.singletonList(componentIdentity));

        return HTTP2Request.build(requestEntity).get();
    }

    /**
     * Find components for the current user matching an identity and component
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .inlineVariables(Arrays.asList(componentIdentity, scenarioIdentity));

        return checkNonNullIterationLatest(requestEntity);
    }

    /**
     * Checks size of axes entries is not null and empty before proceeding
     *
     * @param requestEntity - the request body
     * @return response object
     */
    private ResponseWrapper<ComponentIteration> checkNonNullIterationLatest(RequestEntity requestEntity) {
        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 100L;
        final long MAX_WAIT_TIME = 180L;
        ResponseWrapper<ComponentIteration> axesEntriesResponse;
        int axesEntries = 0;

        do {
            axesEntriesResponse = HTTP2Request.build(requestEntity).get();
            try {
                axesEntries = axesEntriesResponse.getResponseEntity().getResponse().getScenarioMetadata().getAxesEntries().size();
                TimeUnit.MILLISECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException | NullPointerException e) {
                logger.error(e.getMessage());
            }
        } while ((axesEntries == 0) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

        return axesEntriesResponse;
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
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, CostResponse.class)
                .inlineVariables(Arrays.asList(componentIdentity, scenarioIdentity));

        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 5L;
        final long MAX_WAIT_TIME = 180L;
        String scenarioState;
        ResponseWrapper<CostResponse> scenarioRepresentation;

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        do {
            scenarioRepresentation = HTTP2Request.build(requestEntity).get();
            scenarioState = scenarioRepresentation.getResponseEntity().getScenarioState();
            try {
                TimeUnit.SECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (scenarioState.equals(transientState.toUpperCase()) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

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
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENT_BY_COMPONENT_SCENARIO_IDS, CostResponse.class)
                .inlineVariables(Arrays.asList(componentIdentity, scenarioIdentity))
                .body("costingInputs",
                    new CostRequest().setAnnualVolume(5500)
                        .setBatchSize(458)
                        .setMaterialName("Aluminum, Stock, ANSI 1050A")
                        .setProcessGroupName("Sheet Metal")
                        .setProductionLife(5.0)
                        .setVpeName("aPriori USA"));

        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * Gets the hoops image
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ImageResponse> getHoopsImage(String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS, ImageResponse.class)
                .inlineVariables(Arrays.asList(componentIdentity, scenarioIdentity));

        return HTTP2Request.build(requestEntity).get();
    }
}
