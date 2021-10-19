package com.apriori.cidappapi.utils;

import static org.junit.Assert.assertEquals;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.request.request.PublishRequest;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.User;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ImageResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.UncostedComponents;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CidAppTestUtil {

    private String token = null;

    /**
     * Adds a new component
     *
     * @param componentName   - the part name
     * @param scenarioName    - the scenario name
     * @param resourceFile    - the process group
     * @param userCredentials - the user credentials
     * @return response object
     */
    public Item postCssComponents(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {

        return postCssComponents(componentName, scenarioName, resourceFile, getToken(userCredentials));
    }

    /**
     * Adds a new component
     *
     * @param componentName - the part name
     * @param scenarioName  - the scenario name
     * @return responsewrapper
     */
    public Item postCssComponents(String componentName, String scenarioName, String resourceFile, UserCredentials userCredentials) {
        token = getToken(userCredentials);

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(resourceFile), componentName)))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName))
                .token(token);

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<Item> itemResponse = new UncostedComponents().getUnCostedCssComponent(componentName, scenarioName, token);

        return itemResponse.get(0);
    }

    /**
     * Adds a new component
     *
     * @param scenarioName  - the scenario name
     * @param componentName - the part name
     * @return responsewrapper
     */
    public Item postCssComponents(String componentName, String scenarioName, File resourceFile, String token) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", resourceFile))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName))
                .token(token);

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<Item> itemResponse = new UncostedComponents().getUnCostedCssComponent(componentName, scenarioName, token);

        return itemResponse.get(0);
    }

    /**
     * Gets css component
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param scenarioState   - the scenario state
     * @param userCredentials - user credentials
     * @return response object
     */
    public List<Item> getCssComponent(String componentName, String scenarioName, ScenarioStateEnum scenarioState, UserCredentials userCredentials) {
        return new UncostedComponents().getCssComponent(componentName, scenarioName, getToken(userCredentials), scenarioState);
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENTS, GetComponentResponse.class);

        return HTTPRequest.build(requestEntity).get();
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
                .inlineVariables(componentIdentity);

        return HTTPRequest.build(requestEntity).get();
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
                .inlineVariables(componentIdentity, scenarioIdentity);

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
            axesEntriesResponse = HTTPRequest.build(requestEntity).get();
            try {
                axesEntries = axesEntriesResponse.getResponseEntity().getResponse().getScenarioMetadata().getAxesEntries().size();
                TimeUnit.MILLISECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException | NullPointerException e) {
                log.error(e.getMessage());
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
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(String transientState, String componentIdentity, String scenarioIdentity) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentIdentity, scenarioIdentity);

        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 5L;
        final long MAX_WAIT_TIME = 180L;
        String scenarioState;
        ResponseWrapper<ScenarioResponse> scenarioRepresentation;

        waitSeconds(2);
        do {
            scenarioRepresentation = HTTPRequest.build(requestEntity).get();
            scenarioState = scenarioRepresentation.getResponseEntity().getScenarioState();
            waitSeconds(POLLING_INTERVAL);
        } while (scenarioState.equals(transientState.toUpperCase()) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

        return scenarioRepresentation;
    }

    /**
     * Waits for specified time
     *
     * @param seconds - the seconds
     */
    private void waitSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get scenario representation of a published part
     *
     * @param terminalScenarioState - the terminal state
     * @param lastAction            - the last action
     * @param published             - scenario published
     * @param userCredentials       - the user credentials
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(Item cssItem, ScenarioStateEnum terminalScenarioState, String lastAction, boolean published, UserCredentials userCredentials) {
        final int SOCKET_TIMEOUT = 240000;
        String componentName = cssItem.getComponentIdentity();
        String scenarioName = cssItem.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentName, scenarioName)
                .token(getToken(userCredentials))
                .socketTimeout(SOCKET_TIMEOUT);

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioRepresentation = HTTPRequest.build(requestEntity).get();

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final ScenarioResponse scenarioResponse = scenarioRepresentation.getResponseEntity();

                if (scenarioResponse.getScenarioState().contains("FAILED")) {
                    throw new RuntimeException(String.format("Processing has failed for component name: %s, scenario name: %s", componentName, scenarioName));
                }
                if (scenarioResponse.getScenarioState().equals(terminalScenarioState.getState()) && scenarioResponse.getLastAction().equals(lastAction) && scenarioResponse.getPublished() == published) {
                    assertEquals("The component response should be okay.", HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                    return scenarioRepresentation;
                }
            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d seconds.",
                componentName, scenarioName, WAIT_TIME)
        );
    }

    /**
     * Get token
     *
     * @param userCredentials - the user credentials
     * @return string
     */
    private String getToken(UserCredentials userCredentials) {
        return token = userCredentials == null ? new JwtTokenUtil().retrieveJwtToken() : new JwtTokenUtil(userCredentials).retrieveJwtToken();
    }

    /**
     * Post to cost a component
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> postCostComponent(String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENT_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentIdentity, scenarioIdentity)
                .body("costingInputs",
                    CostRequest.builder().annualVolume(5500)
                        .batchSize(458)
                        .materialName("Aluminum, Stock, ANSI 1050A")
                        .processGroupName("Sheet Metal")
                        .productionLife(5.0)
                        .vpeName("aPriori USA")
                        .build()
                );

        return HTTPRequest.build(requestEntity).post();
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
                .inlineVariables(componentIdentity, scenarioIdentity);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Post cost a scenario
     *
     * @param componentName      - the component name
     * @param scenarioName       - the scenario name
     * @param componentId        - the component id
     * @param scenarioId         - the scenario id
     * @param processGroupEnum   - the process group
     * @param digitalFactoryEnum - the digital factory
     * @param mode               - the material mode
     * @param materialName       - the material name
     * @param userCredentials    - the user credentials
     * @return scenario object
     */
    public List<Item> postCostScenario(String componentName, String scenarioName, String componentId, String scenarioId, ProcessGroupEnum processGroupEnum, DigitalFactoryEnum digitalFactoryEnum, String mode, String materialName, UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(getToken(userCredentials))
                .inlineVariables(componentId, scenarioId)
                .body("costingInputs", CostRequest.builder()
                    .costingTemplateIdentity(getCostingTemplateId(processGroupEnum, digitalFactoryEnum, mode, materialName, userCredentials).getIdentity())
                    .deleteTemplateAfterUse(true)
                    .build()
                );
        HTTPRequest.build(requestEntity).post();

        return getCssComponent(componentName, scenarioName, ScenarioStateEnum.COST_COMPLETE, userCredentials);
    }

    /**
     * Get costing template id
     *
     * @param processGroupEnum   - the process group
     * @param digitalFactoryEnum - the digital factory
     * @param mode               - the material mode
     * @param materialName       - the material name
     * @param userCredentials    - the user credentials
     * @return scenario object
     */
    private Scenario getCostingTemplateId(ProcessGroupEnum processGroupEnum, DigitalFactoryEnum digitalFactoryEnum, String mode, String materialName, UserCredentials userCredentials) {
        return postCostingTemplate(processGroupEnum, digitalFactoryEnum, mode, materialName, userCredentials);
    }


    /**
     * Post costing template
     *
     * @param processGroupEnum   - the process group
     * @param digitalFactoryEnum - the digital factory
     * @param mode               - the material mode
     * @param materialName       - the material name
     * @param userCredentials    - the user credentials
     * @return scenario object
     */
    private Scenario postCostingTemplate(ProcessGroupEnum processGroupEnum, DigitalFactoryEnum digitalFactoryEnum, String mode, String materialName, UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COSTING_TEMPLATES, Scenario.class)
                .token(getToken(userCredentials))
                .body("costingTemplate", CostRequest.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .vpeName(digitalFactoryEnum.getDigitalFactory())
                    .materialMode(mode.toUpperCase())
                    .materialName(materialName)
                    .annualVolume(5500)
                    .productionLife(5.0)
                    .batchSize(458)
                    .propertiesToReset(null)
                    .build());

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * Post publish scenario
     *
     * @param item            - the item
     * @param componentId     - the component id
     * @param scenarioId      - the scenario id
     * @param userCredentials - the user credentials
     * @return scenarioresponse object
     */
    public ResponseWrapper<ScenarioResponse> postPublishScenario(Item item, String componentId, String scenarioId, UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_PUBLISH_SCENARIO, ScenarioResponse.class)
                .token(getToken(userCredentials))
                .inlineVariables(componentId, scenarioId)
                .body("scenario", PublishRequest.builder()
                    .assignedTo(getCurrentUser(userCredentials).getIdentity())
                    .costMaturity("Initial".toUpperCase())
                    .override(false)
                    .status("New".toUpperCase())
                    .build()
                );
        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(item, ScenarioStateEnum.COST_COMPLETE, "PUBLISH", true, userCredentials);
    }

    /**
     * Get current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    private User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_CURRENT_USER, User.class)
            .token(getToken(userCredentials));

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }
}
