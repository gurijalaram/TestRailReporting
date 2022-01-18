package com.apriori.cidappapi.utils;

import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING_FAILED;
import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.request.request.PublishRequest;
import com.apriori.cidappapi.entity.request.request.ScenarioRequest;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.entity.response.PeopleResponse;
import com.apriori.cidappapi.entity.response.PersonResponse;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.User;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.CopyScenarioResponse;
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
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CidAppTestUtil {

    /**
     * POST new component
     *
     * @param componentName - the part name
     * @param scenarioName  - the scenario name
     * @return Item
     */
    public Item postCssComponent(String componentName, String scenarioName, String resourceFile, UserCredentials userCredentials) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(resourceFile), componentName)))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName))
                .token(userCredentials.getToken());

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<Item> itemResponse = new UncostedComponents().getUnCostedCssComponent(componentName, scenarioName, userCredentials);

        return itemResponse.get(0);
    }

    /**
     * POST new component
     *
     * @param componentName   - the part name
     * @param scenarioName    - the scenario name
     * @param resourceFile    - the resource file
     * @param userCredentials - the user credentials
     * @return Item
     */
    public Item postCssComponent(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", resourceFile))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName))
                .token(userCredentials.getToken());

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<Item> itemResponse = new UncostedComponents().getUnCostedCssComponent(componentName, scenarioName, userCredentials);

        return itemResponse.get(0);
    }

    /**
     * GET css component
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param scenarioState   - the scenario state
     * @param userCredentials - user credentials
     * @return list of Item
     */
    public List<Item> getCssComponent(String componentName, String scenarioName, ScenarioStateEnum scenarioState, UserCredentials userCredentials) {
        return new UncostedComponents().getCssComponent(componentName, scenarioName, userCredentials, scenarioState);
    }

    /**
     * GET components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENTS, GetComponentResponse.class);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET components for the current user matching an identity
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
     * GET components for the current user matching an identity and component
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
                axesEntries = axesEntriesResponse.getResponseEntity().getScenarioMetadata().getAxesEntries().size();
                TimeUnit.MILLISECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException | NullPointerException e) {
                log.error(e.getMessage());
            }
        } while ((axesEntries == 0) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

        return axesEntriesResponse;
    }

    /**
     * GET scenario representation
     *
     * @param transientState    - the impermanent state
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(String transientState, String componentIdentity, String scenarioIdentity, UserCredentials userCredentials) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentIdentity, scenarioIdentity)
                .token(userCredentials.getToken());

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
     * GET scenario representation of a published part
     *
     * @param lastAction      - the last action
     * @param published       - scenario published
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(Item cssItem, String lastAction, boolean published, UserCredentials userCredentials) {
        final int SOCKET_TIMEOUT = 240000;
        String componentName = cssItem.getComponentIdentity();
        String scenarioName = cssItem.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentName, scenarioName)
                .token(userCredentials.getToken())
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

                final Optional<ScenarioResponse> scenarioResponse = Optional.ofNullable(scenarioRepresentation.getResponseEntity());

                scenarioResponse.filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()))
                    .ifPresent(y -> {
                        throw new RuntimeException(String.format("Processing has failed for Component ID: %s, Scenario ID: %s", componentName, scenarioName));
                    });

                if (scenarioResponse.isPresent() &&
                    scenarioResponse.get().getLastAction().equals(lastAction) &&
                    scenarioResponse.get().getPublished() == published) {

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
     * GET scenario representation of a published part
     *
     * @param scenarioRepresentationBuilder - the scenario representation builder
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(ScenarioRepresentationBuilder scenarioRepresentationBuilder) {
        final int SOCKET_TIMEOUT = 240000;
        String componentName = scenarioRepresentationBuilder.getItem().getComponentIdentity();
        String scenarioName = scenarioRepresentationBuilder.getItem().getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentName, scenarioName)
                .token(scenarioRepresentationBuilder.getUser().getToken())
                .socketTimeout(SOCKET_TIMEOUT);

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = HTTPRequest.build(requestEntity).get();

        assertEquals("The component response should be okay.", HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());
        return scenarioRepresentation;
    }


    /**
     * POST to cost a component
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
                        .processGroupName(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
                        .productionLife(5.0)
                        .digitalFactory(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                        .build()
                );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET hoops image
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
     * POST to cost a scenario
     *
     * @param componentInfoBuilder - the cost component object
     * @return list of scenario items
     */
    public List<Item> postCostScenario(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId())
                .body("costingInputs",
                    CostRequest.builder()
                        .costingTemplateIdentity(
                            getCostingTemplateId(componentInfoBuilder)
                                .getIdentity())
                        .deleteTemplateAfterUse(true)
                        .build());

        HTTPRequest.build(requestEntity).post();

        return getCssComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName(), ScenarioStateEnum.COST_COMPLETE, componentInfoBuilder.getUser());
    }

    public ResponseWrapper<Scenario> postCopyScenario(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
            .token(componentInfoBuilder.getUser().getToken())
            .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId())
            .body("scenarioName",
                ScenarioRequest.builder()
                    .scenarioName(componentInfoBuilder.getScenarioName())
                    .build());

        return HTTPRequest.build(requestEntity).post();

        //return getCssComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName(), ScenarioStateEnum.NOT_COSTED, componentInfoBuilder.getUser());
    }

    /**
     * GET to find components for the current user matching an identity and component
     *
     * @param componentInfoBuilder - the cost component object
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(ComponentInfoBuilder componentInfoBuilder) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET costing template id
     *
     * @return scenario object
     */
    private Scenario getCostingTemplateId(ComponentInfoBuilder componentInfoBuilder) {
        return postCostingTemplate(componentInfoBuilder);
    }


    /**
     * POST costing template
     *
     * @return scenario object
     */
    private Scenario postCostingTemplate(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_COSTING_TEMPLATES, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .body("costingTemplate", CostRequest.builder()
                    .processGroupName(componentInfoBuilder.getProcessGroup().getProcessGroup())
                    .digitalFactory(componentInfoBuilder.getDigitalFactory().getDigitalFactory())
                    .materialMode(componentInfoBuilder.getMode().toUpperCase())
                    .materialName(componentInfoBuilder.getMaterial())
                    .annualVolume(5500)
                    .productionLife(5.0)
                    .batchSize(458)
                    .propertiesToReset(null)
                    .build());

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * POST to publish scenario
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
                .token(userCredentials.getToken())
                .inlineVariables(componentId, scenarioId)
                .body("scenario", PublishRequest.builder()
                    .assignedTo(getCurrentUser(userCredentials).getIdentity())
                    .costMaturity("Initial".toUpperCase())
                    .override(false)
                    .status("New".toUpperCase())
                    .build()
                );
        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(item, "PUBLISH", true, userCredentials);
    }

    /**
     * GET current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    private User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_CURRENT_USER, User.class)
            .token(userCredentials.getToken());

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current person
     *
     * @param userCredentials - the user credentials
     * @return person object
     */
    public PersonResponse getCurrentPerson(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.GET_CURRENT_PERSON, PeopleResponse.class)
            .token(userCredentials.getToken())
            .inlineVariables(userCredentials.getUsername());

        ResponseWrapper<PeopleResponse> peopleResponse = HTTPRequest.build(requestEntity).get();
        return peopleResponse.getResponseEntity().getItems().get(0);
    }
}
