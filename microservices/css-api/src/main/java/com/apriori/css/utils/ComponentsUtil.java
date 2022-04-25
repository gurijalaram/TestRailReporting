package com.apriori.css.utils;

import com.apriori.css.entity.builder.ComponentInfoBuilder;
import com.apriori.css.entity.enums.CidAppAPIEnum;
import com.apriori.css.entity.request.ComponentRequest;
import com.apriori.css.entity.response.*;
import com.apriori.css.entity.response.componentiteration.ComponentIteration;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ComponentsUtil {

    /**
     * POST cad files
     *
     * @param componentBuilder - the component object
     * @return cad file response object
     */
    public ResponseWrapper<CadFilesResponse> postCadFiles(ComponentInfoBuilder componentBuilder) {
        RequestEntity requestEntity =
                RequestEntityUtil.init(CidAppAPIEnum.CAD_FILES, CadFilesResponse.class)
                        .multiPartFiles(new MultiPartFiles().use("cadFiles", componentBuilder.getResourceFile()))
                        .token(componentBuilder.getUser().getToken());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST new component
     *
     * @param componentBuilder - the component object
     * @return Item
     */
    public ComponentInfoBuilder postComponentQueryCSS(ComponentInfoBuilder componentBuilder) {

        String resourceName = postCadFiles(componentBuilder).getResponseEntity().getCadFiles().stream()
                .map(CadFile::getResourceName).collect(Collectors.toList()).get(0);

        RequestEntity requestEntity =
                RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_CREATE, PostComponentResponse.class)
                        .body("groupItems", Collections.singletonList(ComponentRequest.builder()
                                .filename(componentBuilder.getResourceFile().getName())
                                .override(false)
                                .resourceName(resourceName)
                                .scenarioName(componentBuilder.getScenarioName())
                                .build()))
                        .token(componentBuilder.getUser().getToken());

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();

        assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentBuilder.getComponentName(), componentBuilder.getScenarioName()),
                HttpStatus.SC_OK, responseWrapper.getStatusCode());

//        List<ScenarioItem> scenarioItemResponse = new CssComponent().getUnCostedCssComponent(componentBuilder.getComponentName(), componentBuilder.getScenarioName(), componentBuilder.getUser());
//
//        componentBuilder.setComponentIdentity(scenarioItemResponse.get(0).getComponentIdentity());
//        componentBuilder.setScenarioIdentity(scenarioItemResponse.get(0).getScenarioIdentity());
//        componentBuilder.setScenarioItem(scenarioItemResponse.get(0));

        return componentBuilder;
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
                        .inlineVariables(componentInfo.getComponentIdentity());

        return HTTPRequest.build(requestEntity).get();
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
     * Upload a component
     *
     * @param componentBuilder - the component
     * @return - scenario object
     */
    public ComponentInfoBuilder postComponent(ComponentInfoBuilder componentBuilder) {
        File resourceFile = FileResourceUtil.getCloudFile(componentBuilder.getProcessGroup(), componentBuilder.getComponentName() + componentBuilder.getExtension());

        componentBuilder.setResourceFile(resourceFile);

        return postComponentQueryCSS(componentBuilder);
    }
}
