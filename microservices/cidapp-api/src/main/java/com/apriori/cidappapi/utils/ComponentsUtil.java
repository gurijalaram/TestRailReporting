package com.apriori.cidappapi.utils;

import static com.apriori.css.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.entity.enums.CssSearch.COMPONENT_TYPE_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_STATE_EQ;
import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.ComponentRequest;
import com.apriori.cidappapi.entity.response.CadFile;
import com.apriori.cidappapi.entity.response.CadFilesResponse;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.entity.response.Successes;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ComponentsUtil {


    /**
     * POST cad files
     *
     * @param componentBuilder - the component object
     * @return cad file response object
     */
    public ResponseWrapper<CadFilesResponse> postCadFiles(ComponentInfoBuilder componentBuilder) {
        return postCadFile(componentBuilder, componentBuilder.getResourceFiles());
    }

    /**
     * POST cad files
     *
     * @param componentBuilder - the component object
     * @return cad file response object
     */
    public ResponseWrapper<CadFilesResponse> postCadFile(ComponentInfoBuilder componentBuilder) {
        return postCadFile(componentBuilder, Collections.singletonList(componentBuilder.getResourceFile()));
    }

    /**
     * POST cad files
     *
     * @param componentBuilder - the component object
     * @param files            - the list of files
     * @return cad file response object
     */
    private ResponseWrapper<CadFilesResponse> postCadFile(ComponentInfoBuilder componentBuilder, List<File> files) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.CAD_FILES, CadFilesResponse.class)
                .multiPartFiles(new MultiPartFiles().use("cadFiles", files))
                .token(componentBuilder.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST new component
     *
     * @param componentBuilder - the component object
     * @return PostComponentResponse object with a list of <b>Successes</b> and <b>Failures</b>
     */
    public ResponseWrapper<PostComponentResponse> postComponent(ComponentInfoBuilder componentBuilder) {
        String resourceName = postCadFile(componentBuilder).getResponseEntity().getCadFiles().stream()
            .map(CadFile::getResourceName).collect(Collectors.toList()).get(0);

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
                .body("groupItems",
                    Collections.singletonList(ComponentRequest.builder()
                        .filename(componentBuilder.getResourceFile().getName())
                        .override(componentBuilder.isOverrideScenario())
                        .resourceName(resourceName)
                        .scenarioName(componentBuilder.getScenarioName())
                        .build()))
                .token(componentBuilder.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post new component and query css.  This method will only return a component that has been translated ie. componentType = PART
     * @param componentBuilder - the component object
     * @return response object
     */
    public ScenarioItem postComponentQueryCssPart(ComponentInfoBuilder componentBuilder) {
        postComponent(componentBuilder);

        return new CssComponent().getWaitBaseCssComponents(componentBuilder.getUser(),
            COMPONENT_NAME_EQ.getKey() + componentBuilder.getComponentName(), SCENARIO_NAME_EQ.getKey() + componentBuilder.getScenarioName(), COMPONENT_TYPE_EQ.getKey() + " PART")
            .getResponseEntity()
            .getItems()
            .get(0);
    }

    /**
     * POST new component and query CSS
     *
     * @param componentBuilder - the component object
     * @return response object
     */
    public ComponentInfoBuilder postComponentQueryCSSUncosted(ComponentInfoBuilder componentBuilder) {

        List<Successes> componentSuccesses = postComponent(componentBuilder).getResponseEntity().getSuccesses();

        componentSuccesses.forEach(componentSuccess -> {
            List<ScenarioItem> scenarioItemResponse = getUnCostedComponent(componentSuccess.getFilename().split("\\.", 2)[0], componentSuccess.getScenarioName(),
                componentBuilder.getUser());
            componentBuilder.setComponentIdentity(scenarioItemResponse.get(0).getComponentIdentity());
            componentBuilder.setScenarioIdentity(scenarioItemResponse.get(0).getScenarioIdentity());
            // TODO: 26/10/2022 see where needed and remove/fix up if necessary
            componentBuilder.setScenarioItem(scenarioItemResponse.get(0));
        });

        return componentBuilder;
    }

    /**
     * Gets the uncosted component from CSS
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - user to upload the part
     * @return response object
     */
    public List<ScenarioItem> getUnCostedComponent(String componentName, String scenarioName, UserCredentials userCredentials) {
        List<ScenarioItem> scenarioItem = new CssComponent().getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName,
            SCENARIO_STATE_EQ + ScenarioStateEnum.NOT_COSTED.getState()).getResponseEntity().getItems();

        scenarioItem.stream()
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException(String.format("Expected scenario state to be: %s \nFound: %s", ScenarioStateEnum.NOT_COSTED.getState(),
                    scenarioItem.stream().findFirst().get().getScenarioState())));

        return scenarioItem;
    }

    /**
     * POST new multicomponent
     *
     * @param componentInfoBuilder - the component object
     * @return response object
     */
    public ComponentInfoBuilder postMultiComponentsQueryCss(ComponentInfoBuilder componentInfoBuilder) {
        List<CadFile> resources = new ArrayList<>(postCadFiles(componentInfoBuilder).getResponseEntity().getCadFiles());

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
            .body("groupItems", componentInfoBuilder.getResourceFiles()
                .stream()
                .map(resourceFile ->
                    ComponentRequest.builder()
                        .filename(resourceFile.getName())
                        .override(false)
                        .resourceName(resources.stream()
                            .filter(x -> x.getFilename().equals(resourceFile.getName()))
                            .map(CadFile::getResourceName)
                            .collect(Collectors.toList())
                            .get(0))
                        // TODO: 04/04/2022 cn - need to find a way to make this work for 1 scenario name also
                        .scenarioName(componentInfoBuilder.getScenarioName())
                        .build())
                .collect(Collectors.toList()))
            .token(componentInfoBuilder.getUser().getToken());

        ResponseWrapper<PostComponentResponse> postComponentResponse = HTTPRequest.build(requestEntity).post();

        componentInfoBuilder.setComponent(postComponentResponse.getResponseEntity());

        // TODO: 04/04/2022 cn - may want to do this kind of check in the test so may also be unnecessary
        assertEquals("The component(s) was not uploaded.", HttpStatus.SC_OK, postComponentResponse.getStatusCode());

        List<ScenarioItem> scenarioItemList = postComponentResponse.getResponseEntity().getSuccesses().stream().flatMap(component ->
            getUnCostedComponent(component.getFilename(), component.getScenarioName(), componentInfoBuilder.getUser()).stream()).collect(Collectors.toList());

        scenarioItemList.forEach(scenario -> {
            componentInfoBuilder.setComponentIdentity(scenario.getComponentIdentity());
            componentInfoBuilder.setScenarioIdentity(scenario.getScenarioIdentity());
        });

        componentInfoBuilder.setScenarioItems(scenarioItemList);

        return componentInfoBuilder;
    }

    /**
     * Upload a component
     *
     * @param componentBuilder - the component
     * @return response object
     */
    public ComponentInfoBuilder setFilePostComponentQueryCSS(ComponentInfoBuilder componentBuilder) {
        File resourceFile = FileResourceUtil.getCloudFile(componentBuilder.getProcessGroup(), componentBuilder.getComponentName() + componentBuilder.getExtension());

        componentBuilder.setResourceFile(resourceFile);

        return postComponentQueryCSSUncosted(componentBuilder);
    }

    /**
     * GET components for the current user matching a specified query.
     *
     * @return response object
     */
    public ResponseWrapper<GetComponentResponse> getComponents() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS, GetComponentResponse.class);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET components for the current user matching an identity
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ComponentIdentityResponse> getComponentIdentity(ComponentInfoBuilder componentInfo) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, ComponentIdentityResponse.class)
                .inlineVariables(componentInfo.getComponentIdentity())
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET components for the current user matching an identity ewith an expected Return Code
     *
     * @param componentInfo - the component info builder object
     * @param httpStatus    - The expected return code as an int
     * @return response object
     */
    public ResponseWrapper<Object> getComponentIdentityExpectingStatusCode(ComponentInfoBuilder componentInfo, int httpStatus) {
        final int SOCKET_TIMEOUT = 240000;
        final int METHOD_TIMEOUT = 30;
        final LocalDateTime methodStartTime = LocalDateTime.now();
        String componentId = componentInfo.getComponentIdentity();
        ResponseWrapper<Object> response;
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, null)
                .inlineVariables(componentId)
                .token(componentInfo.getUser().getToken())
                .followRedirection(false)
                .socketTimeout(SOCKET_TIMEOUT);
        do {
            response = HTTPRequest.build(requestEntity).get();
        } while (response.getStatusCode() != httpStatus && Duration.between(methodStartTime, LocalDateTime.now()).getSeconds() <= METHOD_TIMEOUT);
        return response;
    }

    /**
     * GET components for the current user matching an identity and component
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(ComponentInfoBuilder componentInfo) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .inlineVariables(componentInfo.getComponentIdentity(), componentInfo.getScenarioIdentity())
                .token(componentInfo.getUser().getToken());

        return checkNonNullIterationLatest(requestEntity);
    }

    /**
     * GET components for the current user matching an identity ewith an expected Return Code
     *
     * @param componentInfo - the component info builder object
     * @param httpStatus    - The expected return code as an int
     * @return response object
     */
    public ResponseWrapper<Object> getComponentIterationLatestExpectingStatusCode(ComponentInfoBuilder componentInfo, int httpStatus) {
        final int SOCKET_TIMEOUT = 240000;
        final int METHOD_TIMEOUT = 30;
        final LocalDateTime methodStartTime = LocalDateTime.now();
        String componentId = componentInfo.getComponentIdentity();
        String scenarioId = componentInfo.getScenarioIdentity();
        ResponseWrapper<Object> response;
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, null)
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
}
