package com.apriori.cidappapi.utils;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING_FAILED;
import static com.apriori.utils.enums.ScenarioStateEnum.transientState;
import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.entity.request.GroupPublishRequest;
import com.apriori.cidappapi.entity.request.Options;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.request.ScenarioAssociationGroupItems;
import com.apriori.cidappapi.entity.request.ScenarioAssociationsRequest;
import com.apriori.cidappapi.entity.request.ScenarioRequest;
import com.apriori.cidappapi.entity.response.GroupCostResponse;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifest;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ScenariosUtil {

    @Getter
    private ComponentsUtil componentsUtil = new ComponentsUtil();

    /**
     * GET scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(ComponentInfoBuilder componentInfo) {
        final String componentName = componentInfo.getComponentName();
        final String scenarioName = componentInfo.getScenarioName();

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioRepresentation = scenarioRequestEntity(componentInfo);

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final Optional<ScenarioResponse> scenarioResponse = Optional.ofNullable(scenarioRepresentation.getResponseEntity());

                scenarioResponse.filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()))
                    .ifPresent(y -> {
                        throw new RuntimeException(String.format("Processing has failed for Component ID: %s, Scenario ID: %s", componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity()));
                    });

                if (scenarioResponse.isPresent() && transientState.stream().noneMatch(x -> x.getState().equals(scenarioResponse.get().getScenarioState()))) {

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
     * GET scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @param scenarioState - the scenario state
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenarioRepresentation(ComponentInfoBuilder componentInfo, ScenarioStateEnum scenarioState) {
        final String componentName = componentInfo.getComponentName();
        final String scenarioName = componentInfo.getScenarioName();

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioRepresentation = scenarioRequestEntity(componentInfo);

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final Optional<ScenarioResponse> scenarioResponse = Optional.ofNullable(scenarioRepresentation.getResponseEntity());

                if (scenarioState != PROCESSING_FAILED) {
                    scenarioResponse.filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()))
                        .ifPresent(y -> {
                            throw new RuntimeException(String.format("Processing has failed for Component ID: %s, Scenario ID: %s", componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity()));
                        });
                }

                if (scenarioResponse.isPresent() && scenarioResponse.get().getScenarioState().equalsIgnoreCase(scenarioState.getState())) {

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
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getPublishedScenarioRepresentation(ComponentInfoBuilder componentInfo, String lastAction, boolean published) {
        final String componentName = componentInfo.getComponentName();
        final String scenarioName = componentInfo.getScenarioName();

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioRepresentation = scenarioRequestEntity(componentInfo);

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final Optional<ScenarioResponse> scenarioResponse = Optional.ofNullable(scenarioRepresentation.getResponseEntity());

                scenarioResponse.filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()))
                    .ifPresent(y -> {
                        throw new RuntimeException(String.format("Processing has failed for Component ID: %s, Scenario ID: %s", componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity()));
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
     * Call GET on Scenario Representation by Component Endpoint with an expected Return Code
     *
     * @param componentInfo - The component info builder object
     * @param httpStatus    - The expected return code as an int
     * @return response - A response object
     */
    public ResponseWrapper<Object> getScenarioRepresentationExpectingStatusCode(ComponentInfoBuilder componentInfo, int httpStatus) {
        final int SOCKET_TIMEOUT = 240000;
        final int METHOD_TIMEOUT = 30;
        final LocalDateTime methodStartTime = LocalDateTime.now();
        String componentId = componentInfo.getComponentIdentity();
        String scenarioId = componentInfo.getScenarioIdentity();
        ResponseWrapper<Object> response;
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, null)
                .inlineVariables(componentId, scenarioId)
                .token(componentInfo.getUser().getToken())
                .followRedirection(false)
                .socketTimeout(SOCKET_TIMEOUT);
        do {
            response = HTTPRequest.build(requestEntity).get();
        } while (response.getStatusCode() != httpStatus && Duration.between(methodStartTime, LocalDateTime.now()).getSeconds() <= METHOD_TIMEOUT);
        return response;
    }

    private ResponseWrapper<ScenarioResponse> scenarioRequestEntity(ComponentInfoBuilder componentInfo) {
        final int SOCKET_TIMEOUT = 240000;
        String componentId = componentInfo.getComponentIdentity();
        String scenarioId = componentInfo.getScenarioIdentity();

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentId, scenarioId)
                .token(componentInfo.getUser().getToken())
                .socketTimeout(SOCKET_TIMEOUT);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * POST to cost a component
     *
     * @param componentInfo - the component object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> postCostComponent(ComponentInfoBuilder componentInfo) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("costingInputs",
                    CostRequest.builder().annualVolume(5500)
                        .batchSize(458)
                        .materialName("Aluminum, Stock, ANSI 1050A")
                        .processGroupName(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
                        .productionLife(5.0)
                        .digitalFactory(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                        .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST to cost a scenario
     *
     * @param componentInfo - the cost component object
     * @return list of scenario items
     */
    public ResponseWrapper<ScenarioResponse> postCostScenario(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("costingInputs",
                    CostRequest.builder()
                        .costingTemplateIdentity(
                            getCostingTemplateId(componentInfo)
                                .getIdentity())
                        .deleteTemplateAfterUse(true)
                        .build());

        HTTPRequest.build(requestEntity).post();

        return getScenarioRepresentation(componentInfo);
    }

    /**
     * Post to Copy a Scenario
     *
     * @param componentInfo - the copy component object
     * @return response object
     */
    public ResponseWrapper<Scenario> postCopyScenario(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("scenario",
                    ScenarioRequest.builder()
                        .scenarioName(componentInfo.getScenarioName())
                        .build())
                .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to Edit a scenario/assembly (with a scenario name that already exists)
     *
     * @param componentInfo - the copy component object
     * @param forkRequest   - the request object
     * @return response object
     */
    public ResponseWrapper<Scenario> postEditScenario(ComponentInfoBuilder componentInfo, ForkRequest forkRequest) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("scenario", ForkRequest.builder()
                    .scenarioName(forkRequest.getScenarioName())
                    .override(forkRequest.getOverride())
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to edit group of scenarios
     *
     * @param componentInfo         - the component info object
     * @param forkRequest           - the fork request
     * @param componentScenarioName - component and scenario name
     * @return response object
     */
    public ResponseWrapper<ScenarioSuccessesFailures> postEditGroupScenarios(ComponentInfoBuilder componentInfo, ForkRequest forkRequest, String... componentScenarioName) {

        List<String[]> componentScenarioNames = Arrays.stream(componentScenarioName).map(x -> x.split(",")).collect(Collectors.toList());
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        for (String[] componentScenario : componentScenarioNames) {
            if (componentInfo.getSubComponents().stream()
                .anyMatch(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))) {

                subComponentInfo.add(componentInfo.getSubComponents().stream()
                    .filter(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))
                    .collect(Collectors.toList()).get(0));
            }
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIOS, ScenarioSuccessesFailures.class)
                .body(ForkRequest.builder()
                    .scenarioName(forkRequest.getScenarioName())
                    .override(forkRequest.getOverride())
                    .groupItems(subComponentInfo
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity(component.getComponentIdentity())
                            .scenarioIdentity(component.getScenarioIdentity())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to edit group of scenarios
     *
     * @param componentInfo         - the component info object
     * @param forkRequest           - the fork request
     * @param componentScenarioName - component and scenario name
     * @return response object
     */
    public ResponseWrapper<ScenarioSuccessesFailures> postEditPublicGroupScenarios(ComponentInfoBuilder componentInfo, ForkRequest forkRequest, String... componentScenarioName) {

        List<String[]> componentScenarioNames = Arrays.stream(componentScenarioName).map(x -> x.split(",")).collect(Collectors.toList());
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        for (String[] componentScenario : componentScenarioNames) {
            if (componentInfo.getSubComponents().stream()
                .anyMatch(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))) {

                new CssComponent().getComponentParts(componentInfo.getUser(), COMPONENT_NAME_EQ.getKey() + componentScenario[0],
                        SCENARIO_NAME_EQ.getKey() + componentScenario[1])
                    .forEach(o -> new CssComponent().getComponentParts(componentInfo.getUser(), COMPONENT_NAME_EQ.getKey() + o.getComponentName(),
                        SCENARIO_NAME_EQ.getKey() + componentScenario[1]));

                subComponentInfo.add(componentInfo.getSubComponents().stream()
                    .filter(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))
                    .collect(Collectors.toList()).get(0));
            }
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIOS, ScenarioSuccessesFailures.class)
                .body(ForkRequest.builder()
                    .scenarioName(forkRequest.getScenarioName())
                    .override(forkRequest.getOverride())
                    .groupItems(subComponentInfo
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity(component.getComponentIdentity())
                            .scenarioIdentity(component.getScenarioIdentity())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to cost a group of scenarios
     *
     * @param componentInfo - A number of copy component objects
     * @return response object
     */
    public ResponseWrapper<GroupCostResponse> postGroupCostScenarios(ComponentInfoBuilder componentInfo, String... componentScenarioName) {

        List<String[]> componentScenarioNames = Arrays.stream(componentScenarioName).map(x -> x.split(",")).collect(Collectors.toList());
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        for (String[] componentScenario : componentScenarioNames) {
            if (componentInfo.getSubComponents().stream()
                .anyMatch(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))) {

                subComponentInfo.add(componentInfo.getSubComponents().stream()
                    .filter(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))
                    .collect(Collectors.toList()).get(0));
            }
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GROUP_COST_COMPONENTS, GroupCostResponse.class)
                .body(GroupCostRequest.builder()
                    .costingTemplateIdentity(getCostingTemplateId(componentInfo.getSubComponents().get(0)).getIdentity())
                    .groupItems(subComponentInfo
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity(component.getComponentIdentity())
                            .scenarioIdentity(component.getScenarioIdentity())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken())
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to cost a group of null scenarios
     *
     * @param componentInfo - the component info object
     * @return response object
     */
    public ResponseWrapper<ErrorMessage> postGroupCostNullScenarios(ComponentInfoBuilder componentInfo) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GROUP_COST_COMPONENTS, ErrorMessage.class)
                .body(GroupCostRequest.builder()
                    .costingTemplateIdentity("")
                    .groupItems(Collections.singletonList(GroupItems.builder()
                        .componentIdentity(null)
                        .scenarioIdentity(null)
                        .build()))
                    .build())
                .token(componentInfo.getUser().getToken())
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to cost a group of scenarios and expect error
     *
     * @param componentInfo - A number of copy component objects
     * @return response object
     */
    public ResponseWrapper<ErrorMessage> postIncorrectGroupCostScenarios(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GROUP_COST_COMPONENTS, ErrorMessage.class)
                .body(GroupCostRequest.builder()
                    .costingTemplateIdentity((componentInfo.getSettings().getUseEmptyCostingTemplateID() ? "" : getCostingTemplateId(componentInfo).getIdentity()))
                    .groupItems(componentInfo.getSubComponents()
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity((component.getSettings().getUseEmptyComponentID() ? "" : component.getComponentIdentity()))
                            .scenarioIdentity((component.getSettings().getUseEmptyScenarioID() ? "" : component.getScenarioIdentity()))
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET costing template id
     *
     * @return scenario object
     */
    private Scenario getCostingTemplateId(ComponentInfoBuilder componentInfo) {
        return postCostingTemplate(componentInfo);
    }

    /**
     * POST costing template
     *
     * @return scenario object
     */
    private Scenario postCostingTemplate(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES, Scenario.class)
                .token(componentInfo.getUser().getToken())
                .body("costingTemplate", CostRequest.builder()
                    .processGroupName(componentInfo.getProcessGroup().getProcessGroup())
                    .digitalFactory(componentInfo.getDigitalFactory().getDigitalFactory())
                    .materialMode(componentInfo.getMode().toUpperCase())
                    .materialName(componentInfo.getMaterial())
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
     * @param componentInfo - the component info builder object
     * @return - scenarioResponse object
     */
    public ResponseWrapper<ScenarioResponse> postPublishScenario(ComponentInfoBuilder componentInfo) {
        publishScenario(componentInfo, ScenarioResponse.class, HttpStatus.SC_CREATED);

        return getPublishedScenarioRepresentation(componentInfo, "PUBLISH", true);
    }

    /**
     * POST to publish scenario
     *
     * @param componentInfo - the component info builder object
     * @param klass         - the  class
     * @param <T>           - the generic return type
     * @return generic object
     */
    public <T> ResponseWrapper<ScenarioResponse> publishScenario(ComponentInfoBuilder componentInfo, Class<T> klass, int expectedResponseCode) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.PUBLISH_SCENARIO, klass)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("scenario", PublishRequest.builder()
                    .assignedTo(new PeopleUtil().getCurrentUser(componentInfo.getUser()).getIdentity())
                    .costMaturity("Initial".toUpperCase())
                    .override(false)
                    .status("New".toUpperCase())
                    .build())
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to publish group of scenarios
     *
     * @param groupPublishRequest   - the group publish request object
     * @param componentScenarioName - component and scenario name
     * @return response object
     */
    public ResponseWrapper<ScenarioSuccessesFailures> postPublishGroupScenarios(GroupPublishRequest groupPublishRequest, String... componentScenarioName) {

        List<String[]> componentScenarioNames = Arrays.stream(componentScenarioName).map(x -> x.split(",")).collect(Collectors.toList());
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        for (String[] componentScenario : componentScenarioNames) {
            ScenarioItem component = new CssComponent().getComponentParts(groupPublishRequest.getComponentInfo().getUser(), COMPONENT_NAME_EQ.getKey() + componentScenario[0],
                    SCENARIO_NAME_EQ.getKey() + componentScenario[1])
                .stream()
                .filter(o -> o.getScenarioIterationKey().getWorkspaceId().equals(groupPublishRequest.getWorkspaceId()))
                .findFirst()
                .get();

            subComponentInfo.add(ComponentInfoBuilder.builder()
                .componentName(component.getComponentName())
                .scenarioName(component.getScenarioName())
                .componentIdentity(component.getComponentIdentity())
                .scenarioIdentity(component.getScenarioIdentity())
                .build());
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.PUBLISH_SCENARIOS, ScenarioSuccessesFailures.class)
                .body(PublishRequest.builder()
                    .groupItems(subComponentInfo
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity(component.getComponentIdentity())
                            .scenarioIdentity(component.getScenarioIdentity())
                            .build())
                        .collect(Collectors.toList()))
                    .options(Options.builder()
                        .scenarioName(groupPublishRequest.getPublishRequest().getScenarioName())
                        .override(groupPublishRequest.getPublishRequest().getOverride())
                        .costMaturity(groupPublishRequest.getPublishRequest().getCostMaturity().toUpperCase())
                        .status(groupPublishRequest.getPublishRequest().getStatus().toUpperCase())
                        .build())
                    .build())
                .token(groupPublishRequest.getComponentInfo().getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to publish group scenarios
     *
     * @param publishRequest - the group publish request object
     * @return response object
     */
    public ResponseWrapper<ScenarioSuccessesFailures> postSimplePublishGroupScenarios(PublishRequest publishRequest) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.PUBLISH_SCENARIOS, ScenarioSuccessesFailures.class)
                .body(PublishRequest.builder()
                    .groupItems(publishRequest.getGroupItems()
                        .stream()
                        .map(component -> GroupItems.builder()
                            .componentIdentity(component.getComponentIdentity())
                            .scenarioIdentity(component.getScenarioIdentity())
                            .build())
                        .collect(Collectors.toList()))
                    .options(Options.builder()
                        .scenarioName(publishRequest.getScenarioName())
                        .override(publishRequest.getOverride())
                        .costMaturity(publishRequest.getCostMaturity().toUpperCase())
                        .status(publishRequest.getStatus().toUpperCase())
                        .build())
                    .build())
                .token(publishRequest.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Upload and Publish a subcomponent/assembly
     *
     * @param componentInfo - the copy component object
     * @return response object
     */
    public ComponentInfoBuilder postAndPublishComponent(ComponentInfoBuilder componentInfo) {
        ComponentInfoBuilder postComponentResponse = componentsUtil.setFilePostComponentQueryCSS(componentInfo);

        postPublishScenario(postComponentResponse);

        return postComponentResponse;
    }

    /**
     * Calls an api with the DELETE verb.
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @param userCredentials   - the user credentials
     * @param <T>               - the generic return type
     * @return generic object
     */
    public <T> ResponseWrapper<ErrorMessage> deleteScenario(String componentIdentity, String scenarioIdentity, UserCredentials userCredentials) {

        final RequestEntity deleteRequest =
            genericDeleteRequest(userCredentials, CidAppAPIEnum.DELETE_SCENARIO, null, componentIdentity, scenarioIdentity);

        HTTPRequest.build(deleteRequest).delete();

        RequestEntity scenarioRequest =
            genericDeleteRequest(userCredentials, CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, null, componentIdentity, scenarioIdentity);

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioResponse = HTTPRequest.build(scenarioRequest).get();

                if (!scenarioResponse.getBody().contains("response")) {

                    RequestEntity requestEntity =
                        genericDeleteRequest(userCredentials, CidAppAPIEnum.DELETE_SCENARIO, ErrorMessage.class, componentIdentity, scenarioIdentity);

                    return HTTPRequest.build(requestEntity).get();
                }
            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component identity: %s, with scenario identity: %s, after %d seconds.",
                componentIdentity, scenarioIdentity, WAIT_TIME)
        );
    }

    /**
     * Calls an api with the GET verb.
     *
     * @param componentIdentity - the component identity
     * @param scenarioIdentity  - the scenario identity
     * @param userCredentials   - the user credentials
     * @param <T>               - the generic return type
     * @return generic object
     */
    public <T> ResponseWrapper<ErrorMessage> getDelete(String componentIdentity, String scenarioIdentity, UserCredentials userCredentials) {

        RequestEntity scenarioRequest =
            genericDeleteRequest(userCredentials, CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, null, componentIdentity, scenarioIdentity);

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ResponseWrapper<ScenarioResponse> scenarioResponse = HTTPRequest.build(scenarioRequest).get();

                if (!scenarioResponse.getBody().contains("response")) {

                    RequestEntity requestEntity =
                        genericDeleteRequest(userCredentials, CidAppAPIEnum.DELETE_SCENARIO, ErrorMessage.class, componentIdentity, scenarioIdentity);

                    return HTTPRequest.build(requestEntity).get();
                }
            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component identity: %s, with scenario identity: %s, after %d seconds.",
                componentIdentity, scenarioIdentity, WAIT_TIME)
        );
    }

    private <T> RequestEntity genericDeleteRequest(UserCredentials userCredentials, CidAppAPIEnum endPoint, Class<T> klass, String componentId, String scenarioId) {
        final int SOCKET_TIMEOUT = 240000;

        return RequestEntityUtil.init(endPoint, klass)
            .token(userCredentials.getToken())
            .inlineVariables(componentId, scenarioId)
            .socketTimeout(SOCKET_TIMEOUT);
    }

    /**
     * GET the manifest for scenario
     *
     * @param componentInfo - the component info builder object
     * @return - response object
     */
    public ResponseWrapper<ScenarioManifest> getScenarioManifest(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.MANIFEST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, ScenarioManifest.class)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Filters the scenario manifest and returns a list of scenario manifest subcomponents
     *
     * @param componentInfo - the component info builder object
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return - response object
     */
    public List<ScenarioManifestSubcomponents> filterScenarioManifest(ComponentInfoBuilder componentInfo, String componentName, String scenarioName) {
        return getScenarioManifest(componentInfo)
            .getResponseEntity()
            .getSubcomponents()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(componentName) && o.getScenarioName().equalsIgnoreCase(scenarioName))
            .collect(Collectors.toList());
    }

    /**
     * PATCH scenario associations
     *
     * @param componentInfo         - the component info builder object
     * @param excluded              - boolean
     * @param componentScenarioName - component and scenario name
     * @return response object
     */
    public ResponseWrapper<AssociationSuccessesFailures> patchAssociations(ComponentInfoBuilder componentInfo, boolean excluded, String... componentScenarioName) {
        ResponseWrapper<ScenarioManifest> scenarioManifestResponse = getScenarioManifest(componentInfo);
        List<ScenarioManifestSubcomponents> scenarioAssociationsRequests = new ArrayList<>();

        final List<String[]> componentScenarioNames = Arrays.stream(componentScenarioName).map(x -> x.split(",")).collect(Collectors.toList());

        for (String[] componentScenario : componentScenarioNames) {

            scenarioAssociationsRequests.add(scenarioManifestResponse.getResponseEntity().getSubcomponents().stream()
                .filter(o -> o.getComponentName().equalsIgnoreCase(componentScenario[0].trim()) && o.getScenarioName().equalsIgnoreCase(componentScenario[1].trim()))
                .collect(Collectors.toList()).get(0));
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_ASSOCIATIONS, AssociationSuccessesFailures.class)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body(ScenarioAssociationsRequest.builder()
                    .groupItems(scenarioAssociationsRequests
                        .stream()
                        .map(component -> ScenarioAssociationGroupItems.builder()
                            .scenarioAssociationIdentity(component.getScenarioAssociationIdentity())
                            .childScenarioIdentity(component.getScenarioIdentity())
                            .occurrences(component.getOccurrences())
                            .excluded(excluded)
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * PATCH scenario associations
     *
     * @param componentInfo                 - the component info builder object
     * @param scenarioManifestSubcomponents - the scenario manifest subcomponents
     * @return response object
     */
    public <T> ResponseWrapper<T> patchAssociations(ComponentInfoBuilder componentInfo, List<ScenarioAssociationGroupItems> scenarioManifestSubcomponents, Class<T> klass) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_ASSOCIATIONS, klass)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body(ScenarioAssociationsRequest.builder()
                    .groupItems(scenarioManifestSubcomponents
                        .stream()
                        .map(component -> ScenarioAssociationGroupItems.builder()
                            .scenarioAssociationIdentity(component.getScenarioAssociationIdentity())
                            .childScenarioIdentity(component.getChildScenarioIdentity())
                            .occurrences(component.getOccurrences())
                            .excluded(component.getExcluded())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * PATCH scenario association and POST to cost scenario
     *
     * @param componentInfo         - the component info builder object
     * @param excluded              - boolean
     * @param componentScenarioName - component and scenario name
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> patchAssociationsAndCost(ComponentInfoBuilder componentInfo, boolean excluded, String... componentScenarioName) {
        patchAssociations(componentInfo, excluded, componentScenarioName);
        return postCostScenario(componentInfo);
    }

    /**
     * Checks if the subcomponent is excluded
     *
     * @param componentInfo - the component info builder object
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return boolean
     */
    public boolean isSubcomponentExcluded(ComponentInfoBuilder componentInfo, String componentName, String scenarioName) {
        return getScenarioManifest(componentInfo).getResponseEntity().getSubcomponents().stream()
            .filter(x -> x.getComponentName().equalsIgnoreCase(componentName) && x.getScenarioName().equalsIgnoreCase(scenarioName))
            .map(ScenarioManifestSubcomponents::getExcluded).findFirst().get();
    }
}
