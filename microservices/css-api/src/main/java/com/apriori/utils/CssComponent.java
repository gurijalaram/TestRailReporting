package com.apriori.utils;

import static org.junit.Assert.assertEquals;

import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

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
        QueryParams queryParams = new QueryParams();

        return getCssComponent(componentName, scenarioName, userCredentials, queryParams.use(getParams(paramKeysValues)));
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
     * @param queryParams     - the query form params
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getCssComponent(String componentName, String scenarioName, UserCredentials userCredentials, QueryParams queryParams) {

        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_NAME, CssComponentResponse.class)
            .inlineVariables(componentName.split("\\.")[0].toUpperCase(), scenarioName)
            .token(userCredentials.getToken())
            .queryParams(queryParams)
            .socketTimeout(SOCKET_TIMEOUT);

        return getComponentPart(componentName, scenarioName, requestEntity);
    }

    /**
     * Calls an api with GET verb.  This method will only return parts that have been translated ie. where componentType is known
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

                ResponseWrapper<CssComponentResponse> cssComponentResponse = getBaseCssComponents(requestEntity);

                assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, cssComponentResponse.getStatusCode()),
                    HttpStatus.SC_OK, cssComponentResponse.getStatusCode());

                if (cssComponentResponse.getResponseEntity().getItems().stream()
                    .noneMatch(o -> o.getComponentType().equalsIgnoreCase("unknown")) &&

                    cssComponentResponse.getResponseEntity().getItems().stream()
                        .allMatch(o -> ScenarioStateEnum.terminalState.stream()
                            .anyMatch(x -> x.getState().equalsIgnoreCase(o.getScenarioState())))) {

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
     * Calls an api with GET verb
     *
     * @param userCredentials - the query form params
     * @param queryKeyValue   - the key value pair
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getCssComponentsQueryParams(UserCredentials userCredentials, String... queryKeyValue) {
        QueryParams queryParams = new QueryParams();

        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, CssComponentResponse.class)
            .token(userCredentials.getToken())
            .queryParams(queryParams.use(getParams(queryKeyValue)))
            .socketTimeout(SOCKET_TIMEOUT);

        return getBaseCssComponents(requestEntity);
    }

    private Map<String, String> getParams(String... queryKeyValue) {
        List<String[]> paramKeyValue = Arrays.stream(queryKeyValue).map(o -> o.split(",")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        paramKeyValue.forEach(o -> paramMap.put(o[0].trim().concat("[EQ]"), o[1].trim()));
        return paramMap;
    }

    /**
     * Calls an api with GET verb
     *
     * @param userCredentials -the query form params
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getBaseCssComponents(UserCredentials userCredentials) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, CssComponentResponse.class)
            .token(userCredentials.getToken())
            .socketTimeout(SOCKET_TIMEOUT);

        return getBaseCssComponents(requestEntity);
    }

    /**
     * Calls an api with GET verb
     *
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<CssComponentResponse> getBaseCssComponents(RequestEntity requestEntity) {
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                ResponseWrapper<CssComponentResponse> cssComponentResponse = HTTPRequest.build(requestEntity).get();

                if (cssComponentResponse.getResponseEntity().getItems().size() > 0) {

                    return cssComponentResponse;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(String.format("Failed to get component after %d seconds", WAIT_TIME));
    }
}
