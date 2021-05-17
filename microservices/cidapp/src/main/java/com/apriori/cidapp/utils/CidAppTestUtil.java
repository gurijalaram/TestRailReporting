package com.apriori.cidapp.utils;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.cidapp.entity.enums.CidAppAPIEnum;
import com.apriori.cidapp.entity.enums.CssAPIEnum;
import com.apriori.cidapp.entity.request.CostRequest;
import com.apriori.cidapp.entity.response.ComponentIdentityResponse;
import com.apriori.cidapp.entity.response.GetComponentResponse;
import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.cidapp.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidapp.entity.response.css.CssComponentResponse;
import com.apriori.cidapp.entity.response.scenarios.CostResponse;
import com.apriori.cidapp.entity.response.scenarios.ImageResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
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

    static {
        RequestEntityUtil.useTokenForRequests(token);
    }

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
        //        url = String.format(serviceUrl, "components");
        //        RequestEntity requestEntity = RequestEntity.init(url, PostComponentResponse.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
        //            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), partName)))
        //            .setFormParams(new FormParams().use("filename", partName)
        //                .use("override", "false")
        //                .use("scenarioName", scenarioName));

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), partName)))
                .formParams(new FormParams().use("filename", partName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName));


        return HTTP2Request.build(requestEntity).post();
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        //        url = String.format(serviceUrl, "components");
        //        RequestEntity requestEntity = RequestEntity.init(url, GetComponentResponse.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

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
        //        url = String.format(serviceUrl, String.format("components/%s", identity));
        //        RequestEntity requestEntity = RequestEntity.init(url, ComponentIdentityResponse.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

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
        //        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s/iterations/latest", componentIdentity, scenarioIdentity));
        //        RequestEntity requestEntity = RequestEntity.init(url, ComponentIteration.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

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
        //        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s", componentIdentity, scenarioIdentity));
        //        RequestEntity requestEntity = RequestEntity.init(url, CostResponse.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

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
            scenarioState = scenarioRepresentation.getResponseEntity().getResponse().getScenarioState();
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
        //        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s/cost", componentIdentity, scenarioIdentity));
        //
        //        RequestEntity requestEntity = RequestEntity.init(url, CostResponse.class)
        //            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
        //            .setBody("costingInputs",
        //                new CostRequest().setAnnualVolume(5500)
        //                    .setBatchSize(458)
        //                    .setMaterialName("Aluminum, Stock, ANSI 1050A")
        //                    .setProcessGroupName("Sheet Metal")
        //                    .setProductionLife(5.0)
        //                    .setVpeName("aPriori USA"));

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
        //        url = String.format(serviceUrl, String.format("components/%s/scenarios/%s/hoops-image", componentIdentity, scenarioIdentity));

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS, ImageResponse.class)
                .inlineVariables(Arrays.asList(componentIdentity, scenarioIdentity));

        return HTTP2Request.build(requestEntity).get();
    }

    public ResponseWrapper<CssComponentResponse> getUnCostedCssComponents(String componentName, String scenarioName) {

        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.GET_COMPONENT_BY_COMPONENT_SCENARIO_NAMES, CssComponentResponse.class)
            .inlineVariables(Arrays.asList(componentName, scenarioName));

        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 5L;
        final long MAX_WAIT_TIME = 180L;
        String verifiedState = "NOT_COSTED";
        String scenarioState;
        ResponseWrapper<CssComponentResponse> scenarioRepresentation;

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        do {
            scenarioRepresentation = HTTP2Request.build(requestEntity).get();

            while (scenarioRepresentation.getResponseEntity().getResponse().getItems().isEmpty() && scenarioRepresentation.getResponseEntity().getResponse().getItems() == null) {
                scenarioRepresentation = HTTP2Request.build(requestEntity).get();
            }

            scenarioState = scenarioRepresentation.getResponseEntity().getResponse().getItems().get(0).getScenarioState();
            try {
                TimeUnit.SECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!scenarioState.equals(verifiedState.toUpperCase()) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);
        return scenarioRepresentation;
    }
}
