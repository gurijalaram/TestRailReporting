package com.apriori.sds.util;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;
import static org.junit.Assert.assertFalse;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Application;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.request.PublishRequest;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.entity.response.CostingTemplatesItems;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class SDSTestUtil extends TestUtil {

    protected static UserCredentials testingUser;
    protected static String appApplicationContext;
    protected static Set<ScenarioItem> scenariosToDelete = new HashSet<>();
    private static ScenarioItem testingComponent;


    @BeforeClass
    public static void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser = UserUtil.getUser("admin"));
        RequestEntityUtil.useTokenForRequests(testingUser.getToken());
    }

    @AfterClass
    public static void clearTestingData() {
        if (!scenariosToDelete.isEmpty()) {
            scenariosToDelete.forEach(component -> {
                removeTestingScenario(component.getComponentIdentity(), component.getScenarioIdentity());
            });
        }

        scenariosToDelete = new HashSet<>();
        testingComponent = null;
    }

    /**
     * Get component id
     *
     * @return string
     */
    protected static String getComponentId() {
        return getTestingComponent().getComponentIdentity();
    }

    /**
     * Get scenario id
     *
     * @return string
     */
    protected static String getScenarioId() {
        return getTestingComponent().getScenarioIdentity();
    }

    /**
     * Get iteration id
     *
     * @return string
     */
    protected static String getIterationId() {
        return getTestingComponent().getIterationIdentity();
    }

    /**
     * Post testing component
     *
     * @return object
     */
    protected static ScenarioItem postTestingComponentAndAddToRemoveList() {
        final String componentName = "AGC0-LP-700144754.prt.1";
        final ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;

        return postPart(componentName, processGroup);
    }

    /**
     * Remove testing component
     *
     * @param componentId - component id
     * @param scenarioId  - scenario id
     * @return response object
     */
    protected static void removeTestingScenario(final String componentId, final String scenarioId) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null)
                .inlineVariables(componentId, scenarioId)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Lazy init for Testing component to avoid it if it is not necessary
     *
     * @return
     */
    protected static ScenarioItem getTestingComponent() {
        if (testingComponent == null) {
            testingComponent = postTestingComponentAndAddToRemoveList();
        }

        return testingComponent;
    }

    /**
     * Adds a new part
     *
     * @param componentName - the part name
     * @return responsewrapper
     */
    protected static ScenarioItem postPart(String componentName, ProcessGroupEnum processGroup) {
        final String uniqueScenarioName = new GenerateStringUtil().generateScenarioName();
        final File fileToUpload = FileResourceUtil.getS3FileAndSaveWithUniqueName(componentName,
            processGroup
        );

        ComponentInfoBuilder componentInfo = ComponentInfoBuilder.builder()
            .resourceFiles(
                Collections.singletonList(fileToUpload)
            )
            .scenarioName(uniqueScenarioName)
            .user(testingUser)
            .build();

        String uploadedComponentResourceName = new ComponentsUtil()
            .postCadFiles(componentInfo)
            .get(0)
            .getResourceName();

        String fileMetadataIdentity = FileManagementController
            .uploadFileWithResourceName(testingUser, fileToUpload, uploadedComponentResourceName)
            .getIdentity();

        return postComponent(PostComponentRequest.builder()
            .fileMetadataIdentity(fileMetadataIdentity)
            .scenarioName(uniqueScenarioName)
            .override(false)
            .build(), fileToUpload.getName());
    }

    /**
     * Adds a new Roll up
     *
     * @param componentName - the roll up name
     * @param scenarioName  - the scenario name
     * @return responsewrapper
     */
    protected static ScenarioItem postRollUp(String componentName, String scenarioName) {
        final String uniqueComponentName = new GenerateStringUtil().generateComponentName(componentName);

        final PostComponentRequest postComponentRequest = PostComponentRequest.builder()
            .scenarioName(scenarioName)
            .override(false)
            .componentName(uniqueComponentName)
            .componentType("ROLLUP")
            .build();

        return postComponent(postComponentRequest, uniqueComponentName);
    }

    /**
     * Gets the uncosted component from CSS
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - user to upload the part
     * @return response object
     */
    public static List<ScenarioItem> getUnCostedComponent(String componentName, String scenarioName, UserCredentials userCredentials) {
        return new CssComponent().getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName.split("\\.", 2)[0], SCENARIO_NAME_EQ.getKey() + scenarioName,
            SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED);
    }

    protected static ScenarioItem postComponent(final PostComponentRequest postComponentRequest, final String componentName) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .headers(getContextHeaders())
                .token(testingUser.getToken())
                .body("component", postComponentRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).post();

        List<ScenarioItem> scenarioItemResponse = getUnCostedComponent(componentName, postComponentRequest.getScenarioName(),
            testingUser);

        scenariosToDelete.add(scenarioItemResponse.get(0));
        return scenarioItemResponse.get(0);
    }

    public static Map<String, String> getContextHeaders() {
        return new HashMap<String, String>() {
            {
                put("ap-application-context", getApApplicationContext());
                put("ap-cloud-context", testingUser.getCloudContext());
            }
        };
    }

    // TODO z: can be migrated to AuthorizationUtil if make this decision based on usage this functionality outside sds
    private static String getApApplicationContext() {

        if (appApplicationContext != null) {
            return appApplicationContext;
        }
        return appApplicationContext = initApApplicationContext();
    }

    private static String initApApplicationContext() {
        ResponseWrapper<Customers> customersResponse = HTTPRequest.build(
            RequestEntityUtil.init(CDSAPIEnum.CUSTOMERS, Customers.class)
                .token(testingUser.getToken())

        ).get();

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = HTTPRequest.build(
            RequestEntityUtil.init(CDSAPIEnum.CUSTOMERS_APPLICATION_BY_CUSTOMER_ID, Applications.class)
                .inlineVariables(customer.getIdentity())
                .token(testingUser.getToken())
        ).get();

        Application cidApplication = responseApplications.getResponseEntity().getItems().stream().filter(app -> app.getServiceName().equals("CID"))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("CID application was not found."));

        return cidApplication.getIdentity();
    }

    /**
     * GET scenario representation
     *
     * @param scenarioItem    - the scenario object
     * @param userCredentials - the user credentials
     * @return response object
     */
    protected static ResponseWrapper<Scenario> getScenarioRepresentation(ScenarioItem scenarioItem, UserCredentials userCredentials) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS, Scenario.class)
                .inlineVariables(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity())
                .token(userCredentials.getToken());

        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 10L;
        final long MAX_WAIT_TIME = 180L;
        String scenarioState;
        ResponseWrapper<Scenario> scenarioRepresentation;

        waitSeconds(2);
        do {
            scenarioRepresentation = HTTPRequest.build(requestEntity).get();
            scenarioState = scenarioRepresentation.getResponseEntity().getScenarioState();
            waitSeconds(POLLING_INTERVAL);
        } while (scenarioState.equals(scenarioItem.getScenarioState().toUpperCase()) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

        return scenarioRepresentation;
    }

    protected CostingTemplate getFirstCostingTemplate() {
        List<CostingTemplate> costingTemplates = getCostingTemplates();
        assertFalse("To get CostingTemplate it should present in response", costingTemplates.isEmpty());
        return costingTemplates.get(0);
    }


    protected List<CostingTemplate> getCostingTemplates() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_COSTING_TEMPLATES, CostingTemplatesItems.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CostingTemplatesItems> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems();
    }

    protected void addScenarioToDelete(final String identity) {
        scenariosToDelete.add(ScenarioItem.builder()
            .componentIdentity(getComponentId())
            .scenarioIdentity(identity)
            .build()
        );
    }

    /**
     * POST to cost a scenario
     *
     * @param componentInfoBuilder - the cost component object
     * @return list of scenario items
     */
    public static List<ScenarioItem> postCostScenario(ComponentInfoBuilder componentInfoBuilder) {
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

        return getUnCostedComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName(), componentInfoBuilder.getUser());
    }

    /**
     * GET costing template id
     *
     * @return scenario object
     */
    private static Scenario getCostingTemplateId(ComponentInfoBuilder componentInfoBuilder) {
        return postCostingTemplate(componentInfoBuilder);
    }

    /**
     * POST costing template
     *
     * @return scenario object
     */
    private static Scenario postCostingTemplate(ComponentInfoBuilder componentInfoBuilder) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COSTING_TEMPLATES, Scenario.class)
                .token(componentInfoBuilder.getUser().getToken())
                .body("costingTemplate", componentInfoBuilder.getCostingTemplate());

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * Waits for specified time
     *
     * @param seconds - the seconds
     */
    private static void waitSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Gets the scenario by customer scenario id
     *
     * @param componentIdentity - the component id
     * @param scenarioIdentity  - the scenario id
     * @return scenario object
     */
    protected Scenario getScenarioByCustomerScenarioIdentity(String componentIdentity, final String scenarioIdentity) {
        if (componentIdentity == null) {
            componentIdentity = getComponentId();
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS, Scenario.class)
                .inlineVariables(
                    componentIdentity, scenarioIdentity
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity();
    }

    /**
     * Waits for the scenario to be in an expected state
     *
     * @param scenario - scenario object
     * @return boolean
     */
    private boolean isScenarioStateAsExpected(final Scenario scenario) {
        final String currentScenarioState = scenario.getScenarioState().toUpperCase();

        return currentScenarioState.equals(ScenarioStateEnum.NOT_COSTED.getState())
            || currentScenarioState.equals(ScenarioStateEnum.COST_COMPLETE.getState());
    }

    /**
     * Gets scenario in a ready state
     *
     * @param componentIdentity - the component id
     * @param scenarioIdentity  - the scenario id
     * @return scenario object
     */
    protected Scenario getReadyToWorkScenario(final String componentIdentity, final String scenarioIdentity) {
        final int attemptsCount = 15;
        final int secondsToWait = 10;
        int currentCount = 0;
        Scenario scenario;

        do {
            waitSeconds(secondsToWait);
            scenario = this.getScenarioByCustomerScenarioIdentity(componentIdentity, scenarioIdentity);

            if (scenario.getScenarioState().toUpperCase().contains("FAILED")) {
                throw new IllegalStateException(String.format("Scenario failed state: %s. Scenario Id: %s",
                    scenario.getScenarioState(), scenario.getIdentity())
                );
            }

            if (isScenarioStateAsExpected(scenario)) {
                return scenario;
            }
        } while (currentCount++ < attemptsCount);

        throw new IllegalArgumentException(
            String.format("Failed to get scenario by identity: %s, after %d attempts with period in %d seconds.",
                scenarioIdentity, attemptsCount, secondsToWait)
        );
    }

    /**
     * Shallow publish an Assembly
     *
     * @param componentInfoBuilder - the component info builder object
     * @return - scenario object
     */
    protected <T> ResponseWrapper<Scenario> publishAssembly(ComponentInfoBuilder componentInfoBuilder, Class<T> klass, Integer expectedResponseCode) {
        PublishRequest shallowPublishRequest = PublishRequest.builder()
            .assignedTo(componentInfoBuilder.getPublishRequest().getAssignedTo())
            .locked(false)
            .override(false)
            .scenarioName(componentInfoBuilder.getScenarioName())
            .status(componentInfoBuilder.getPublishRequest().getStatus())
            .publishSubComponents(false)
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, klass)
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
                .body("scenario", shallowPublishRequest)
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Publish an assembly expecting a conflict
     *
     * @param componentInfoBuilder - the component info builder object
     * @return - scenario object
     */
    public ResponseWrapper<Scenario> publishAssemblyExpectError(ComponentInfoBuilder componentInfoBuilder, Integer expectedResponseCode) {
        return publishAssembly(componentInfoBuilder, ErrorMessage.class, expectedResponseCode);
    }
}
