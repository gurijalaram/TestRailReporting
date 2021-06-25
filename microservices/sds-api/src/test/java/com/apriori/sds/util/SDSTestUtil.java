package com.apriori.sds.util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.utils.EncodedFileUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.UncostedComponents;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class SDSTestUtil extends TestUtil {

    private static final List<Item> componentsToDelete = new ArrayList<>();
    private static Item testingComponent;

    @AfterClass
    public static void clearTestingData() {
        if (!componentsToDelete.isEmpty()) {
            componentsToDelete.forEach(component -> {
                removeTestingComponent(component.getComponentIdentity(), component.getScenarioIdentity());
            });
        }
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
    protected static Item postTestingComponent() {
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
    protected static void removeTestingComponent(final String componentId, final String scenarioId) {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null)
            .inlineVariables(componentId, scenarioId);

        ResponseWrapper<String> response = HTTP2Request.build(requestEntity).delete();

        assertEquals(String.format("The component with scenario %s, was not removed", scenarioId),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());
    }

    /**
     * Lazy init for Testing component to avoid it if it is not necessary
     * @return
     */
    protected static Item getTestingComponent() {
        if (testingComponent == null) {
            testingComponent = postTestingComponent();
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
    protected static Item postPart(String componentName, String scenarioName, ProcessGroupEnum processGroup) {
        final PostComponentRequest postComponentRequest = PostComponentRequest.builder().filename(componentName)
            .componentName(componentName)
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
    protected static Item postRollUp(String componentName, String scenarioName) {
        final PostComponentRequest postComponentRequest = PostComponentRequest.builder()
            .scenarioName(scenarioName)
            .override(false)
            .componentName(componentName)
            .componentType("ROLLUP")
            .build();

        return postComponent(postComponentRequest);
    }

    protected static Item postComponent(final PostComponentRequest postComponentRequest) {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .body("component", postComponentRequest);

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", postComponentRequest.getComponentName(), postComponentRequest.getScenarioName()),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        List<Item> itemResponse = new UncostedComponents().getUnCostedCssComponent(postComponentRequest.getComponentName(), postComponentRequest.getScenarioName());

        componentsToDelete.add(itemResponse.get(0));
        return itemResponse.get(0);
    }
}
