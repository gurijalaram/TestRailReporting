package com.apriori.utils;

import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

@Slf4j
public class CssComponent {

    FormParams formParams = new FormParams();
    private String itemScenarioState;
    private final int SOCKET_TIMEOUT = 630000;
    private final int POLL_TIME = 2;
    private final int WAIT_TIME = 600;

    /**
     * Calls an api with GET verb
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState, not_costed"
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     * @throws ArrayIndexOutOfBoundsException if only one of the paramKeysValues is supplied eg. "scenarioState" rather than "scenarioState, not_costed"
     */
    public ResponseWrapper<CssComponentResponse> getCssComponentQueryParams(String componentName, String scenarioName, UserCredentials userCredentials, String... paramKeysValues) {

        List<String[]> paramKeyValue = Arrays.stream(paramKeysValues).map(o -> o.split(",")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        paramKeyValue.forEach(o -> paramMap.put(o[0].trim().concat("[EQ]"), o[1].trim()));

        return getCssComponent(componentName, scenarioName, userCredentials, formParams.use(paramMap));
    }

    /**
     * Calls an api with GET verb
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getCssComponent(String componentName, String scenarioName, UserCredentials userCredentials) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_NAME, CssComponentResponse.class)
            .inlineVariables(componentName.split("\\.")[0].toUpperCase(), scenarioName)
            .token(userCredentials.getToken())
            .socketTimeout(SOCKET_TIMEOUT);

        return getComponentPart(componentName, scenarioName, requestEntity);
    }

    /**
     * Calls an api with GET verb
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - the user credentials
     * @param formParams      - the query form params
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getCssComponent(String componentName, String scenarioName, UserCredentials userCredentials, FormParams formParams) {

        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_NAME, CssComponentResponse.class)
            .inlineVariables(componentName.split("\\.")[0].toUpperCase(), scenarioName)
            .token(userCredentials.getToken())
            .formParams(formParams)
            .socketTimeout(SOCKET_TIMEOUT);

        return getComponentPart(componentName, scenarioName, requestEntity);
    }

    /**
     * Calls an api with GET verb
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param requestEntity - the request data
     * @return the response wrapper that contains the response data
     */
    private ResponseWrapper<CssComponentResponse> getComponentPart(String componentName, String scenarioName, RequestEntity requestEntity) {

        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                ResponseWrapper<CssComponentResponse> cssComponentResponse = HTTPRequest.build(requestEntity).get();

                Assert.assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, cssComponentResponse.getStatusCode()),
                    HttpStatus.SC_OK, cssComponentResponse.getStatusCode());

                if (cssComponentResponse.getResponseEntity().getItems().size() > 0 &&
                    cssComponentResponse.getResponseEntity().getItems().stream()
                        .anyMatch(o -> !o.getComponentType().equalsIgnoreCase("unknown"))) {

                    return cssComponentResponse;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d seconds",
            componentName, scenarioName, WAIT_TIME)
        );
    }

    /**
     * Gets a css component specified by workspace id
     *
     * @param workspaceId     - the workspace id
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ScenarioItem getWorkspaceComponent(int workspaceId, String componentName, String scenarioName, UserCredentials userCredentials) {
        return getCssComponent(componentName, scenarioName, userCredentials)
            .getResponseEntity()
            .getItems()
            .stream()
            .filter(o -> o.getScenarioIterationKey().getWorkspaceId().equals(workspaceId))
            .collect(Collectors.toList()).get(0);
    }
}
