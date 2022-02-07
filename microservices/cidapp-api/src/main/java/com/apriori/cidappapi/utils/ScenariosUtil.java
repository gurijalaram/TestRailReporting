package com.apriori.cidappapi.utils;

import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING_FAILED;
import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.request.request.ForkRequest;
import com.apriori.cidappapi.entity.request.request.PublishRequest;
import com.apriori.cidappapi.entity.request.request.ScenarioRequest;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.scenarios.ImageResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.CssComponent;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScenariosUtil {

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
     * GET scenario representation of a published part
     *
     * @param lastAction      - the last action
     * @param published       - scenario published
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(Item cssItem, String lastAction, boolean published, UserCredentials userCredentials) {
        final int SOCKET_TIMEOUT = 240000;
        String componentId = cssItem.getComponentIdentity();
        String scenarioId = cssItem.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentId, scenarioId)
                .token(userCredentials.getToken())
                .socketTimeout(SOCKET_TIMEOUT);

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioRepresentation = HTTPRequest.build(requestEntity).get();

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentId, scenarioId, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final Optional<ScenarioResponse> scenarioResponse = Optional.ofNullable(scenarioRepresentation.getResponseEntity());

                scenarioResponse.filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()))
                    .ifPresent(y -> {
                        throw new RuntimeException(String.format("Processing has failed for Component ID: %s, Scenario ID: %s", componentId, scenarioId));
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
                componentId, scenarioId, WAIT_TIME)
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
        String componentId = scenarioRepresentationBuilder.getItem().getComponentIdentity();
        String scenarioId = scenarioRepresentationBuilder.getItem().getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentId, scenarioId)
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
            RequestEntityUtil.init(CidAppAPIEnum.COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
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

        return new CssComponent().getCssComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName(), componentInfoBuilder.getUser(), componentInfoBuilder.getScenarioState());
    }

    /**
     * Post to Copy a Scenario
     *
     * @param componentInfoBuilder - the copy component object
     * @return response object
     */
    public ResponseWrapper<Scenario> postCopyScenario(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
            .token(componentInfoBuilder.getUser().getToken())
            .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId())
            .body("scenario",
                ScenarioRequest.builder()
                    .scenarioName(componentInfoBuilder.getScenarioName())
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to Edit a scenario/assembly
     *
     * @return response object
     */
    public ResponseWrapper<Scenario> postEditScenario(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId())
                .body("scenario", ForkRequest.builder()
                    .override(false)
                    .build());

        return HTTPRequest.build(requestEntity).post();
    }

    public ResponseWrapper<Scenario> postEditScenario(ComponentInfoBuilder componentInfoBuilder, String scenarioName) {
        final RequestEntity requestEntity =
                RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                        .token(componentInfoBuilder.getUser().getToken())
                        .inlineVariables(componentInfoBuilder.getComponentId(), componentInfoBuilder.getScenarioId())
                        .body("scenario", ForkRequest.builder()
                                .override(true)
                                .scenarioName(scenarioName)
                                .build());

        return HTTPRequest.build(requestEntity).post();
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
    //todo: make this method just user the item object alone as it contains componentId and scenarioId already (or some other type of builder that has all this infomration)
    public ResponseWrapper<ScenarioResponse> postPublishScenario(Item item, String componentId, String scenarioId, UserCredentials userCredentials) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_PUBLISH_SCENARIO, ScenarioResponse.class)
                .token(userCredentials.getToken())
                .inlineVariables(componentId, scenarioId)
                .body("scenario", PublishRequest.builder()
                    .assignedTo(new PeopleUtil().getCurrentUser(userCredentials).getIdentity())
                    .costMaturity("Initial".toUpperCase())
                    .override(false)
                    .status("New".toUpperCase())
                    .build()
                );
        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(item, "PUBLISH", true, userCredentials);
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
}
