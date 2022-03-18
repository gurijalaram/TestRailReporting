package com.apriori.sds.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.entity.response.CostingTemplatesItems;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.utils.CssComponent;
import com.apriori.utils.EncodedFileUtil;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class SDSTestUtil extends TestUtil {

    protected static UserCredentials testingUser;
    protected static Set<ScenarioItem> scenariosToDelete = new HashSet<>();
    private static ScenarioItem testingComponent;

    @BeforeClass
    public static  void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser =  UserUtil.getUser());
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
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "AGC0-LP-700144754.prt.1";
        ProcessGroupEnum processGroup = ProcessGroupEnum.SHEET_METAL;

        return postPart(componentName, scenarioName, processGroup);
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
     * @param scenarioName  - the scenario name
     * @return responsewrapper
     */
    protected static ScenarioItem postPart(String componentName, String scenarioName, ProcessGroupEnum processGroup) {
        final String uniqueComponentName = new GenerateStringUtil().generateComponentName(componentName);

        final PostComponentRequest postComponentRequest = PostComponentRequest.builder().filename(uniqueComponentName)
            .componentName(uniqueComponentName)
            .scenarioName(scenarioName)
            .override(false)
            .fileContents(EncodedFileUtil.encodeFileFromCloudToBase64Binary(componentName, processGroup))
            .build();

        return postComponent(postComponentRequest);
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

        return postComponent(postComponentRequest);
    }

    protected static ScenarioItem postComponent(final PostComponentRequest postComponentRequest) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .body("component", postComponentRequest);

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", postComponentRequest.getComponentName(), postComponentRequest.getScenarioName()),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<ScenarioItem> scenarioItemResponse = new CssComponent().getUnCostedCssComponent(postComponentRequest.getComponentName(), postComponentRequest.getScenarioName(),
            testingUser);

        scenariosToDelete.add(scenarioItemResponse.get(0));
        return scenarioItemResponse.get(0);
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
        final long POLLING_INTERVAL = 5L;
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
