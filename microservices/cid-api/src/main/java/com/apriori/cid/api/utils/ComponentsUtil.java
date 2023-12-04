package com.apriori.cid.api.utils;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.cid.api.models.request.ComponentRequest;
import com.apriori.cid.api.models.response.CadFile;
import com.apriori.cid.api.models.response.CadFilesResponse;
import com.apriori.cid.api.models.response.ComponentIdentityResponse;
import com.apriori.cid.api.models.response.GetComponentResponse;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.MultiPartFiles;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.component.Successes;
import com.apriori.shared.util.models.response.component.PostComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;

import com.google.common.collect.Iterators;
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

    private static final int MAX_FILES = 20;
    private static final int CHUNK_SIZE = 10;
    private static final int POLL_TIME = 1;
    private static final int WAIT_TIME = 570;

    /**
     * POST cad files
     *
     * @param componentInfo - the component object
     * @return cad file response object
     */
    public List<CadFile> postCadFiles(ComponentInfoBuilder componentInfo) {
        if (componentInfo.getResourceFiles().size() > MAX_FILES) {
            throw new RuntimeException("Attempted to upload '" + componentInfo.getResourceFiles().size() + "' files. Only a maximum of '" + MAX_FILES + "' CAD files can be uploaded at the same time");
        }

        List<CadFile> cadFiles = new ArrayList<>();

        Iterators.partition(componentInfo.getResourceFiles().iterator(), CHUNK_SIZE).forEachRemaining(cadFile ->
            cadFiles.addAll(postCadFile(componentInfo, cadFile).getResponseEntity().getCadFiles()));

        return cadFiles;
    }

    /**
     * POST cad files
     *
     * @param componentInfo - the component object
     * @return cad file response object
     */
    public ResponseWrapper<CadFilesResponse> postCadFile(ComponentInfoBuilder componentInfo) {
        return postCadFile(componentInfo, Collections.singletonList(componentInfo.getResourceFile()));
    }

    /**
     * POST cad files
     *
     * @param componentInfo - the component object
     * @param files         - the list of files
     * @return cad file response object
     */
    private ResponseWrapper<CadFilesResponse> postCadFile(ComponentInfoBuilder componentInfo, List<File> files) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.CAD_FILES, CadFilesResponse.class)
                .multiPartFiles(new MultiPartFiles().use("cadFiles", files))
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST subcomponents cad files
     *
     * @param componentInfo - the component object
     * @return cad files response object
     */
    public CadFilesResponse postSubcomponentsCadFiles(ComponentInfoBuilder componentInfo) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.CAD_FILES, CadFilesResponse.class)
                .multiPartFiles(new MultiPartFiles().use("cadFiles", componentInfo.getSubComponents()
                    .stream()
                    .map(ComponentInfoBuilder::getResourceFile)
                    .collect(Collectors.toList())))
                .token(componentInfo.getUser().getToken());

        ResponseWrapper<CadFilesResponse> cadFilesResponse = HTTPRequest.build(requestEntity).post();
        return cadFilesResponse.getResponseEntity();
    }

    /**
     * POST subcomponents
     *
     * @param componentInfo    - the component object
     * @param cadFilesResponse - the cad files
     * @return component response object
     */
    public PostComponentResponse postSubcomponents(ComponentInfoBuilder componentInfo, CadFilesResponse cadFilesResponse) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
                .body("groupItems", cadFilesResponse.getCadFiles()
                    .stream()
                    .map(cadFileResponse -> ComponentRequest.builder()
                        .filename(cadFileResponse.getFilename())
                        .override(componentInfo.getOverrideScenario())
                        .resourceName(cadFileResponse.getResourceName())
                        .scenarioName(componentInfo.getScenarioName())
                        .build())
                    .collect(Collectors.toList()))
                .token(componentInfo.getUser().getToken());

        ResponseWrapper<PostComponentResponse> postComponentResponse = HTTPRequest.build(requestEntity).post();
        return postComponentResponse.getResponseEntity();
    }

    /**
     * POST new component
     *
     * @param componentInfo - the component object
     * @return PostComponentResponse object with a list of <b>Successes</b> and <b>Failures</b>
     */
    public ResponseWrapper<PostComponentResponse> postComponent(ComponentInfoBuilder componentInfo) {
        String resourceName = postCadFile(componentInfo).getResponseEntity().getCadFiles().stream()
            .map(CadFile::getResourceName).collect(Collectors.toList()).get(0);

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
                .body("groupItems",
                    Collections.singletonList(ComponentRequest.builder()
                        .filename(componentInfo.getResourceFile().getName())
                        .override(componentInfo.getOverrideScenario())
                        .resourceName(resourceName)
                        .scenarioName(componentInfo.getScenarioName())
                        .build()))
                .token(componentInfo.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST new component and query CSS
     *
     * @param componentInfo - the component object
     * @return response object
     */
    public ComponentInfoBuilder postComponentQueryCSSUncosted(ComponentInfoBuilder componentInfo) {

        Successes componentSuccess = postComponent(componentInfo).getResponseEntity().getSuccesses().stream().findFirst().get();

        ScenarioItem scenarioItemResponse = getUnCostedComponent(componentSuccess.getFilename().split("\\.", 2)[0], componentSuccess.getScenarioName(),
            componentInfo.getUser()).stream().findFirst().get();

        componentInfo.setComponentIdentity(scenarioItemResponse.getComponentIdentity());
        componentInfo.setScenarioIdentity(scenarioItemResponse.getScenarioIdentity());

        return componentInfo;
    }

    /**
     * Calls an api with POST verb and query CID
     *
     * @param componentInfo - the component object
     * @return response object
     */
    public ComponentInfoBuilder postComponentQueryCID(ComponentInfoBuilder componentInfo) {

        Successes componentSuccess = postComponent(componentInfo).getResponseEntity().getSuccesses().stream().findFirst().get();

        componentInfo.setComponentIdentity(componentSuccess.getComponentIdentity());
        componentInfo.setScenarioIdentity(componentSuccess.getScenarioIdentity());

        ComponentIdentityResponse componentIdentityResponse = getComponentIdentityPart(componentInfo);

        componentInfo.setComponentIdentity(componentIdentityResponse.getIdentity());

        new ScenariosUtil().getScenarioCompleted(componentInfo);

        return componentInfo;
    }

    /**
     * Feeder method for postComponentQueryCID to allow users to upload parts
     * without creating one-shot ComponentInfoBuilders
     *
     * @param componentName    - String of components name
     * @param scenarioName     - String with chosen name for scenario
     * @param processGroupEnum - Enum of components Process Group
     * @param resourceFile     - File to be uploaded
     * @param currentUser      - Current user performing upload
     * @return - ComponentInfoBuilder of created scenario
     */
    public ComponentInfoBuilder postComponentCID(String componentName, String scenarioName, ProcessGroupEnum processGroupEnum, File resourceFile, UserCredentials currentUser) {
        return postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());
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
        List<ScenarioItem> scenarioItem = new CssComponent().getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName);

        scenarioItem
            .stream()
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException(String.format("Expected scenario state to be: %s Found: %s", ScenarioStateEnum.NOT_COSTED.getState(),
                    scenarioItem.stream().findFirst().get().getScenarioState())));

        return scenarioItem;
    }

    /**
     * Calls an api with POST verb to post multiple components
     *
     * @param componentInfo - the component object
     * @return response object
     */
    public List<ScenarioItem> postMultiComponentsQueryCSS(ComponentInfoBuilder componentInfo) {
        List<CadFile> resources = postCadFiles(componentInfo);

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
            .body("groupItems", componentInfo.getResourceFiles()
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
                        .scenarioName(componentInfo.getScenarioName())
                        .build())
                .collect(Collectors.toList()))
            .token(componentInfo.getUser().getToken())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<PostComponentResponse> postComponentResponse = HTTPRequest.build(requestEntity).post();

        componentInfo.setComponent(postComponentResponse.getResponseEntity());

        return postComponentResponse.getResponseEntity().getSuccesses().stream().flatMap(component ->
            getUnCostedComponent(component.getFilename().split("\\.", 2)[0], component.getScenarioName(), componentInfo.getUser())
                .stream()).collect(Collectors.toList());
    }

    /**
     * Calls an api with POST verb to post multiple components
     *
     * @param componentInfo - the component object
     * @return response object
     */
    public List<ComponentIdentityResponse> postMultiComponentsQueryCID(ComponentInfoBuilder componentInfo) {
        List<CadFile> resources = postCadFiles(componentInfo);

        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
            .body("groupItems", componentInfo.getResourceFiles()
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
                        .scenarioName(componentInfo.getScenarioName())
                        .build())
                .collect(Collectors.toList()))
            .token(componentInfo.getUser().getToken())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<PostComponentResponse> postComponentResponse = HTTPRequest.build(requestEntity).post();

        componentInfo.setComponent(postComponentResponse.getResponseEntity());

        return postComponentResponse.getResponseEntity().getSuccesses().stream().map(component ->
            getComponentIdentityPart(ComponentInfoBuilder.builder()
                .componentIdentity(component.getComponentIdentity())
                .user(componentInfo.getUser())
                .build())).collect(Collectors.toList());
    }

    /**
     * Upload a component via CSS
     *
     * @param componentInfo - the component
     * @return response object
     */
    public ComponentInfoBuilder setFilePostComponentQueryCSS(ComponentInfoBuilder componentInfo) {
        File resourceFile = FileResourceUtil.getCloudFile(componentInfo.getProcessGroup(), componentInfo.getComponentName() + componentInfo.getExtension());

        componentInfo.setResourceFile(resourceFile);

        return postComponentQueryCSSUncosted(componentInfo);
    }

    /**
     * Upload a component via CID
     *
     * @param componentInfo - the component
     * @return response object
     */
    public ComponentInfoBuilder setFilePostComponentQueryCID(ComponentInfoBuilder componentInfo) {
        File resourceFile = FileResourceUtil.getCloudFile(componentInfo.getProcessGroup(), componentInfo.getComponentName() + componentInfo.getExtension());

        componentInfo.setResourceFile(resourceFile);

        return postComponentQueryCID(componentInfo);
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
     * Calls an api with GET verb. This method will ONLY get translated parts ie. componentType = Part/Assembly
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ComponentIdentityResponse getComponentIdentityPart(ComponentInfoBuilder componentInfo) {
        final long START_TIME = System.currentTimeMillis() / 1000;

        do {
            try {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                ComponentIdentityResponse componentIdentityResponse = getComponentIdentity(componentInfo);

                if (componentIdentityResponse != null) {

                    //fail fast
                    if (new ScenariosUtil().getScenario(componentInfo).getScenarioState().equalsIgnoreCase(ScenarioStateEnum.PROCESSING_FAILED.getState())) {
                        throw new RuntimeException(String.format("Processing has failed for component name: '%s', component id: '%s', scenario name: '%s'",
                            componentInfo.getComponentName(), componentInfo.getComponentIdentity(), componentInfo.getScenarioName()));
                    }

                    if (!componentIdentityResponse.getComponentType().equalsIgnoreCase("unknown")) {
                        return componentIdentityResponse;
                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();

            } catch (AssertionError a) {
                log.error(a.getMessage());
            }
        } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        throw new RuntimeException(
            String.format("Failed to get uploaded component name: '%s', component id: '%s', scenario name: '%s', after '%d' seconds.",
                componentInfo.getComponentName(), componentInfo.getComponentIdentity(), componentInfo.getScenarioName(), WAIT_TIME));
    }

    /**
     * GET components for the current user matching an identity
     *
     * @param componentInfo - the component info builder object
     * @return response object
     */
    public ComponentIdentityResponse getComponentIdentity(ComponentInfoBuilder componentInfo) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, ComponentIdentityResponse.class)
                .inlineVariables(componentInfo.getComponentIdentity())
                .token(componentInfo.getUser().getToken())
                .followRedirection(true);

        ResponseWrapper<ComponentIdentityResponse> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity();
    }

    /**
     * GET components for the current user matching an identity with an expected Return Code
     *
     * @param componentInfo - the component info builder object
     * @param httpStatus    - The expected return code as an int
     * @return response object
     */
    // TODO: 21/12/2022 method needs revised, may not work as expected
    public ResponseWrapper<Object> getComponentIdentityExpectingStatusCode(ComponentInfoBuilder componentInfo, int httpStatus) {
        final int SOCKET_TIMEOUT = 240000;
        final int METHOD_TIMEOUT = 30;
        final LocalDateTime methodStartTime = LocalDateTime.now();
        String componentId = componentInfo.getComponentIdentity();
        ResponseWrapper<Object> response;
        RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, null)
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
    // TODO: 21/12/2022 why is this method needed or even used here? check IterationsUtil for duplication
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
    // TODO: 21/12/2022 method needs revised, may not work as expected
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
     * Verify scenario custom attribute
     *
     * @param componentIteration Component Latest Iteration
     * @param attributeValue     value to check
     * @return true or false
     */
    public boolean checkScenarioCustomAttribute(ComponentIteration componentIteration, String attributeValue) {
        boolean isCustomAttributeFound = false;
        if (null != componentIteration) {
            isCustomAttributeFound = componentIteration.getScenarioCustomAttributes().stream()
                .anyMatch(a -> a.getValue().toString().contains(attributeValue));
        } else {
            log.error(String.format("Matching Component or scenario identity not found!!"));
        }
        if ((isCustomAttributeFound)) {
            log.info(String.format("Matching scenario custom attribute (%s) found!!", attributeValue));
        } else {
            log.error(String.format("Matching scenario custom attribute (%s) not found!!", attributeValue));
        }
        return isCustomAttributeFound;
    }

    /**
     * Checks size of axes entries is not null and empty before proceeding
     *
     * @param requestEntity - the request body
     * @return response object
     */
    private ResponseWrapper<ComponentIteration> checkNonNullIterationLatest(RequestEntity requestEntity) {
        final long START_TIME = System.currentTimeMillis() / 1000;
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
