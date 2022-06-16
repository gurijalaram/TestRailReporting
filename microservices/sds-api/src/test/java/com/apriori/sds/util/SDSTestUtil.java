package com.apriori.sds.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.entity.response.Application;
import com.apriori.entity.response.Applications;
import com.apriori.fms.controller.FileManagementController;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.entity.response.CostingTemplatesItems;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

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
        String componentName = "AGC0-LP-700144754.prt.1";
        ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;

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
                .inlineVariables(componentId, scenarioId);

        ResponseWrapper<String> response = HTTPRequest.build(requestEntity).delete();

        assertEquals(String.format("The component with scenario %s, was not removed", scenarioId),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());
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

        ComponentInfoBuilder componentInfo = ComponentInfoBuilder.builder()
            .resourceFiles(
                Collections.singletonList(
                    FileResourceUtil.getCloudFile(processGroup, componentName)
                )
            )
            .scenarioName(uniqueScenarioName)
            .user(testingUser)
            .build();

        String uploadedComponentResourceName = new ComponentsUtil()
            .postCadFiles(componentInfo)
            .getResponseEntity()
            .getCadFiles()
            .get(0)
            .getResourceName();

        String fileMetadataIdentity = FileManagementController
            .uploadFileWithResourceName(testingUser, processGroup, componentName, uploadedComponentResourceName)
            .getIdentity();

        return postComponent(PostComponentRequest.builder()
            .fileMetadataIdentity(fileMetadataIdentity)
            .scenarioName(uniqueScenarioName)
            .override(false)
            .build(), componentName);
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

    protected static ScenarioItem postComponent(final PostComponentRequest postComponentRequest, final String componentName) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .headers(getContextHeaders())
                .token(testingUser.getToken())
                .body("component", postComponentRequest);

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, postComponentRequest.getScenarioName()),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<ScenarioItem> scenarioItemResponse = new CssComponent().getUnCostedCssComponent(componentName, postComponentRequest.getScenarioName(),
            testingUser);

        scenariosToDelete.add(scenarioItemResponse.get(0));
        return scenarioItemResponse.get(0);
    }

    public static Map<String, String> getContextHeaders() {
        return new HashMap<String, String>() {{
            put("ap-application-context", getApApplicationContext());
            put("ap-cloud-context", testingUser.getCloudContext());
        }};
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
            RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
                .token(testingUser.getToken())

        ).get();

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = HTTPRequest.build(
            RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_APPLICATIONS, Applications.class)
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
            RequestEntityUtil.init(SDSAPIEnum.GET_COSTING_TEMPLATES, CostingTemplatesItems.class);

        ResponseWrapper<CostingTemplatesItems> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

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

        return new CssComponent().getCssComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName(), componentInfoBuilder.getUser(), componentInfoBuilder.getScenarioState());
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
}
