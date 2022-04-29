package com.apriori.cidappapi.utils;

import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING_FAILED;
import static com.apriori.utils.enums.ScenarioStateEnum.transientGroup;
import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.request.request.ForkRequest;
import com.apriori.cidappapi.entity.request.request.PublishRequest;
import com.apriori.cidappapi.entity.request.request.ScenarioRequest;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.scenarios.ImageResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScenariosUtil {

    private ComponentsUtil componentsUtil = new ComponentsUtil();

    /**
     * GET scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(ComponentInfoBuilder componentInfo) {
        final int SOCKET_TIMEOUT = 240000;
        String componentId = componentInfo.getComponentIdentity();
        String scenarioId = componentInfo.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentId, scenarioId)
                .token(componentInfo.getUser().getToken())
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

                if (scenarioResponse.isPresent() && transientGroup.stream().noneMatch(x -> x.getState().equals(scenarioResponse.get().getScenarioState()))) {

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
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getPublishedScenarioRepresentation(ComponentInfoBuilder componentInfo, String lastAction, boolean published) {
        final int SOCKET_TIMEOUT = 240000;
        String componentId = componentInfo.getComponentIdentity();
        String scenarioId = componentInfo.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentId, scenarioId)
                .token(componentInfo.getUser().getToken())
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
     * POST to cost a component
     *
     * @param componentInfoBuilder - the component object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> postCostComponent(ComponentInfoBuilder componentInfoBuilder) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
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
            RequestEntityUtil.init(CidAppAPIEnum.HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS, ImageResponse.class)
                .inlineVariables(componentIdentity, scenarioIdentity);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * POST to cost a scenario
     *
     * @param componentInfoBuilder - the cost component object
     * @return list of scenario items
     */
    public ResponseWrapper<ScenarioResponse> postCostScenario(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
                .body("costingInputs",
                    CostRequest.builder()
                        .costingTemplateIdentity(
                            getCostingTemplateId(componentInfoBuilder)
                                .getIdentity())
                        .deleteTemplateAfterUse(true)
                        .build());

        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(componentInfoBuilder);
    }

    /**
     * Post to cost a group of scenarios
     *
     * @param componentInfoBuilder - A number of copy component objects
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> postGroupCostScenarios(ComponentInfoBuilder componentInfoBuilder) {
        List<ComponentInfoBuilder> subComponents = componentInfoBuilder.getSubComponents();
        String groupItems = "\"groupItems\": [";
        if (!subComponents.isEmpty()) {
//            subComponents.forEach(
//                subComponent -> {
//                    groupItems += "{'componentIdentity':'" + subComponent.getComponentIdentity() + "',";
//                    groupItems += "'scenarioIdentity':'" + subComponent.getScenarioIdentity() + "'},"; }
//            );
//            groupItems = groupItems.substring(0, groupItems.length() - 1);
//            groupItems += "]";
            for (int i = 0; i < subComponents.size(); i++) {
                groupItems += "{\"componentIdentity\":\"" + subComponents.get(i).getComponentIdentity() + "\",";
                groupItems += "\"scenarioIdentity\":\"" + subComponents.get(i).getScenarioIdentity() + "\"}";
                if (i < subComponents.size() - 1) {
                    groupItems += ",";
                }
            }
            groupItems += "]}";
        }

        String customBody = "{\"costingTemplateIdentity\":\"" + getCostingTemplateId(componentInfoBuilder).getIdentity() + "\", " + groupItems;

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GROUP_COST_COMPONENTS, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .customBody(customBody);

        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(componentInfoBuilder);
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
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
                .body("scenario",
                    ScenarioRequest.builder()
                        .scenarioName(componentInfoBuilder.getScenarioName())
                        .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to Edit a scenario/assembly (with a scenario name that already exists)
     *
     * @param componentInfoBuilder - the copy component object
     * @param forkRequest          - the request object
     * @return response object
     */
    public ResponseWrapper<Scenario> postEditScenario(ComponentInfoBuilder componentInfoBuilder, ForkRequest forkRequest) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
                .body("scenario", forkRequest);

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
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES, Scenario.class)
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
     * @param componentInfoBuilder - the component info builder object
     * @return - scenarioResponse object
     */
    public ResponseWrapper<ScenarioResponse> postPublishScenario(ComponentInfoBuilder componentInfoBuilder) {
        publishScenario(componentInfoBuilder, ScenarioResponse.class);

        return getPublishedScenarioRepresentation(componentInfoBuilder, "PUBLISH", true);
    }

    /**
     * POST to publish scenario
     *
     * @param componentInfoBuilder - the component info builder object
     * @param klass                - the  class
     * @param <T>                  - the generic return type
     * @return generic object
     */
    public <T> ResponseWrapper<ScenarioResponse> publishScenario(ComponentInfoBuilder componentInfoBuilder, Class<T> klass) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.PUBLISH_SCENARIO, klass)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
                .body("scenario", PublishRequest.builder()
                    .assignedTo(new PeopleUtil().getCurrentUser(componentInfoBuilder.getUser()).getIdentity())
                    .costMaturity("Initial".toUpperCase())
                    .override(false)
                    .status("New".toUpperCase())
                    .build()
                );

        return HTTPRequest.build(requestEntity).post();
    }


    /**
     * Upload and Publish a subcomponent/assembly
     *
     * @param component - the copy component object
     * @return - the Item
     */
    public ComponentInfoBuilder postAndPublishComponent(ComponentInfoBuilder component) {
        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponent(component);

        postPublishScenario(postComponentResponse);

        return postComponentResponse;
    }
}
