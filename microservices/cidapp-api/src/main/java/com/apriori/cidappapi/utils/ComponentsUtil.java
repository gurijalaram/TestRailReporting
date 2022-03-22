package com.apriori.cidappapi.utils;

import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.request.request.ComponentRequest;
import com.apriori.cidappapi.entity.response.CadFile;
import com.apriori.cidappapi.entity.response.CadFilesResponse;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
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
     * @param resourceFile     - the resource file
     * @return Item
     */
    public ScenarioItem postComponentQueryCSS(ComponentInfoBuilder componentBuilder, File resourceFile) {

        String resourceName = postCadFiles(componentBuilder).getResponseEntity().getCadFiles().stream()
            .filter(x -> x.getFilename().equals(resourceFile.getName()))
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

        List<ScenarioItem> scenarioItemResponse = new CssComponent().getUnCostedCssComponent(componentBuilder.getComponentName(), componentBuilder.getScenarioName(), componentBuilder.getUser());

        return scenarioItemResponse.get(0);
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
     * @param scenarioItem - the scenario object
     * @return response object
     */
    public ResponseWrapper<ComponentIdentityResponse> getComponentIdentity(ScenarioItem scenarioItem) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, ComponentIdentityResponse.class)
                .inlineVariables(scenarioItem.getComponentIdentity());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET components for the current user matching an identity and component
     *
     * @param scenarioItem    - the scenario item object
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(ScenarioItem scenarioItem, UserCredentials userCredentials) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .inlineVariables(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity())
                .token(userCredentials.getToken());

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
}
