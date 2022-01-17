package com.apriori.cidappapi.utils;

import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.entity.response.Scenario;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.UncostedComponents;
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
     * GET costing template id
     *
     * @return scenario object
     */
    public Scenario getCostingTemplateId(ComponentInfoBuilder componentInfoBuilder) {
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
}
