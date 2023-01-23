package com.apriori.cidappapi.utils;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.entity.request.GroupPublishRequest;
import com.apriori.cidappapi.entity.request.Options;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.request.ScenarioAssociationGroupItems;
import com.apriori.cidappapi.entity.request.ScenarioAssociationsRequest;
import com.apriori.cidappapi.entity.request.ScenarioRequest;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.CostingTemplates;
import com.apriori.cidappapi.entity.response.GroupCostResponse;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.entity.response.scenarios.Routings;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifest;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ScenariosUtil {

    @Getter
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private CssComponent cssComponent = new CssComponent();
    private final int POLL_TIME = 2;
    private final int WAIT_TIME = 240;
    private final int SOCKET_TIMEOUT = 240000;
    private final long START_TIME = System.currentTimeMillis() / 1000;
    private final int METHOD_TIMEOUT = 30;

    /**
     * GET completed scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ScenarioResponse getScenarioCompleted(ComponentInfoBuilder componentInfo) {
        try {
            do {
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                ScenarioResponse scenarioRepresentation = getScenario(componentInfo).getResponseEntity();

                if (scenarioRepresentation != null &&

                    ScenarioStateEnum.terminalState.stream()
                        .anyMatch(o -> o.getState().equalsIgnoreCase(scenarioRepresentation.getScenarioState()))) {

                    return scenarioRepresentation;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d seconds.",
                componentInfo.getComponentName(), componentInfo.getScenarioName(), WAIT_TIME)
        );
    }

    /**
     * GET state of scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @param scenarioState - the scenario state
     * @return response object
     */
    public ScenarioResponse getScenarioState(ComponentInfoBuilder componentInfo, ScenarioStateEnum scenarioState) {
        final ScenarioResponse scenarioRepresentation = getScenarioCompleted(componentInfo);

        if (Objects.equals(scenarioRepresentation.getScenarioState(), scenarioState.getState())) {
            return scenarioRepresentation;
        }
        throw new RuntimeException(String.format("Failed to get the correct state of the component. Expected: '%s' but found '%s'", scenarioState, scenarioRepresentation.getScenarioState()));
    }

    /**
     * GET scenario representation of a published part
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ScenarioResponse getScenarioActioned(ComponentInfoBuilder componentInfo, String lastAction, boolean published) {
        final ScenarioResponse scenarioRepresentation = getScenarioCompleted(componentInfo);

        if (scenarioRepresentation != null &&
            scenarioRepresentation.getLastAction().equals(lastAction) &&
            scenarioRepresentation.getPublished() == published) {

            return scenarioRepresentation;
        }
        throw new RuntimeException("Scenario has not been published");
    }

    /**
     * GET scenario representation of a part
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ScenarioResponse> getScenario(ComponentInfoBuilder componentInfo) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS, ScenarioResponse.class)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .token(componentInfo.getUser().getToken())
                .socketTimeout(SOCKET_TIMEOUT)
                .expectedResponseCode(HttpStatus.SC_OK)
                .followRedirection(true);

        ResponseWrapper<ScenarioResponse> response = null;
        try {
            response = HTTPRequest.build(requestEntity).get();
        } catch (AssertionError e) {

            log.debug(e.getMessage());
            while (response.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                response = HTTPRequest.build(requestEntity).get();
            }
        }

        return response;
    }

    /**
     * Call GET on Scenario Representation by Component Endpoint with an expected Return Code
     *
     * @param componentInfo - The component info builder object
     * @param httpStatus    - The expected return code as an int
     * @return response - A response object
     */
    public ResponseWrapper<Object> getScenarioExpectingStatusCode(ComponentInfoBuilder componentInfo, int httpStatus) {
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

    /**
     * POST to cost a scenario
     *
     * @param componentInfo - the cost component object
     * @return list of scenario items
     */
    public ScenarioResponse postCostScenario(ComponentInfoBuilder componentInfo) {
        CostingTemplate costingTemplate = postCostingTemplate(componentInfo);

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("costingInputs", CostingTemplate.builder()
                    .costingTemplateIdentity(costingTemplate.getIdentity())
                    .deleteTemplateAfterUse(costingTemplate.getDeleteTemplateAfterUse())
                    .build());

        HTTPRequest.build(requestEntity).post();

        return getScenarioCompleted(componentInfo);
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
     * @param componentInfo - the component info object
     * @param forkRequest   - the fork request
     * @param componentName - component name
     * @return response object
     */
    public ScenarioSuccessesFailures postEditGroupScenarios(ComponentInfoBuilder componentInfo, ForkRequest forkRequest, String... componentName) {
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        //iterate the assembly, filter by subcomponent and set the component/scenario Id
        Arrays.stream(componentName).forEach(component -> {
            final ComponentInfoBuilder componentIdentifier = componentInfo.getSubComponents().stream()
                .filter(subcomponent -> subcomponent.getComponentName().equalsIgnoreCase(component))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .get();

            subComponentInfo.add(ComponentInfoBuilder.builder()
                .componentIdentity(componentIdentifier.getComponentIdentity())
                .scenarioIdentity(componentIdentifier.getScenarioIdentity())
                .build());
        });

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

        ResponseWrapper<ScenarioSuccessesFailures> response = HTTPRequest.build(requestEntity).post();

        //query css and set the new scenario Id
        Arrays.stream(componentName).forEach(component -> {
            ScenarioItem scenarioItem = cssComponent.getComponentParts(componentInfo.getUser(),
                    COMPONENT_NAME_EQ.getKey() + component, SCENARIO_NAME_EQ.getKey() + componentInfo.getScenarioName(),
                    CssSearch.SCENARIO_PUBLISHED_EQ.getKey() + false).stream()
                .findFirst().get();

            componentInfo.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(component))
                .forEach(x -> x.setScenarioIdentity(scenarioItem.getScenarioIdentity()));
        });
        return response.getResponseEntity();
    }

    /**
     * Post to edit group of scenarios
     *
     * @param componentInfo - the component info object
     * @param forkRequest   - the fork request
     * @return response object
     */
    public <T> ResponseWrapper<T> postSimpleEditGroupScenarios(ComponentInfoBuilder componentInfo, ForkRequest forkRequest, Class<T> klass) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.EDIT_SCENARIOS, klass)
                .body(ForkRequest.builder()
                    .scenarioName(forkRequest.getScenarioName())
                    .override(forkRequest.getOverride())
                    .groupItems(forkRequest.getGroupItems()
                        .stream()
                        .map(request -> GroupItems.builder()
                            .componentIdentity(request.getComponentIdentity())
                            .scenarioIdentity(request.getScenarioIdentity())
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
     * @param componentName - the component name
     * @return response object
     */
    public ResponseWrapper<GroupCostResponse> postGroupCostScenarios(ComponentInfoBuilder componentInfo, String... componentName) {
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        Arrays.stream(componentName).forEach(component -> subComponentInfo.add(componentInfo.getSubComponents().stream()
            .filter(subcomponent -> subcomponent.getComponentName().equalsIgnoreCase(component))
            .collect(Collectors.toList())
            .stream()
            .findFirst()
            .get()));

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.GROUP_COST_COMPONENTS, GroupCostResponse.class)
                .body(GroupCostRequest.builder()
                    .costingTemplateIdentity(postCostingTemplate(componentInfo.getSubComponents().get(0)).getIdentity())
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
                    .costingTemplateIdentity(componentInfo.getCostingTemplate().getCostingTemplateIdentity())
                    .groupItems(componentInfo.getSubComponents()
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
     * Calls an api with the POST verb
     *
     * @param componentInfo - the component info object
     * @return response object
     */
    public CostingTemplate postCostingTemplate(ComponentInfoBuilder componentInfo) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES, CostingTemplate.class)
                .token(componentInfo.getUser().getToken())
                .body("costingTemplate", componentInfo.getCostingTemplate());

        ResponseWrapper<CostingTemplate> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * Calls an api with the GET verb
     *
     * @param userCredentials - the user credentials
     * @return response object
     */
    public CostingTemplate getCostingTemplateIdentity(UserCredentials userCredentials, String... inlineVariables) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES_ID, CostingTemplate.class)
                .inlineVariables(inlineVariables)
                .token(userCredentials.getToken());

        ResponseWrapper<CostingTemplate> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity();
    }

    /**
     * Calls an api with the GET verb
     *
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<CostingTemplates> getCostingTemplates(UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES, CostingTemplates.class)
                .token(userCredentials.getToken());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * POST to publish scenario
     *
     * @param componentInfo - the component info builder object
     * @return - scenarioResponse object
     */
    public ScenarioResponse postPublishScenario(ComponentInfoBuilder componentInfo) {
        publishScenario(componentInfo, ScenarioResponse.class, HttpStatus.SC_CREATED);

        return getScenarioActioned(componentInfo, "PUBLISH", true);
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
        componentInfo.setPublishRequest(PublishRequest.builder()
            .assignedTo(new PeopleUtil().getCurrentUser(componentInfo.getUser())
                .getIdentity())
            .build());

        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.PUBLISH_SCENARIO, klass)
                .token(componentInfo.getUser().getToken())
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .body("scenario", componentInfo.getPublishRequest())
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to publish group of scenarios
     *
     * @param groupPublishRequest - the group publish request object
     * @param componentInfo       - the component info builder object
     * @param componentName       - component and scenario name
     * @return response object
     */
    public ResponseWrapper<ScenarioSuccessesFailures> postPublishGroupScenarios(GroupPublishRequest groupPublishRequest, ComponentInfoBuilder componentInfo, String... componentName) {
        List<ComponentInfoBuilder> subComponentInfo = new ArrayList<>();

        Arrays.stream(componentName).forEach(component -> {
            final ComponentInfoBuilder componentIdentifier = componentInfo.getSubComponents().stream()
                .filter(subcomponent -> subcomponent.getComponentName().equalsIgnoreCase(component))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .get();

            subComponentInfo.add(ComponentInfoBuilder.builder()
                .componentIdentity(componentIdentifier.getComponentIdentity())
                .scenarioIdentity(componentIdentifier.getScenarioIdentity())
                .user(componentInfo.getUser())
                .build());
        });

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
                        .scenarioName(groupPublishRequest.getComponentInfo().getScenarioName())
                        .override(groupPublishRequest.getPublishRequest().getOverride())
                        .costMaturity(groupPublishRequest.getPublishRequest().getCostMaturity().toUpperCase())
                        .status(groupPublishRequest.getPublishRequest().getStatus().toUpperCase())
                        .build())
                    .build())
                .token(groupPublishRequest.getComponentInfo().getUser().getToken());

        ResponseWrapper<ScenarioSuccessesFailures> response = HTTPRequest.build(requestEntity).post();

        subComponentInfo.forEach(component -> getScenarioActioned(component, "PUBLISH", true));

        return response;
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
        ComponentInfoBuilder postComponentResponse = componentsUtil.setFilePostComponentQueryCID(componentInfo);

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
    public ScenarioResponse patchAssociationsAndCost(ComponentInfoBuilder componentInfo, boolean excluded, String... componentScenarioName) {
        patchAssociations(componentInfo, excluded, componentScenarioName);
        return postCostScenario(componentInfo);
    }

    /**
     * GET scenario routings
     *
     * @param currentUser     - the user details to obtain a token
     * @param inlineVariables - usually component/scenario identity
     * @return response object
     */
    public ResponseWrapper<Routings> getRoutings(UserCredentials currentUser, String... inlineVariables) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.ROUTINGS, Routings.class)
                .inlineVariables(inlineVariables)
                .token(currentUser.getToken());

        return HTTPRequest.build(requestEntity).get();
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
