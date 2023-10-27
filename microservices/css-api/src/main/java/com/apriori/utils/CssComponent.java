package com.apriori.utils;

import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.enums.CssAPIEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.component.ComponentResponse;
import com.apriori.models.response.component.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author cfrith
 */

@Slf4j
public class CssComponent {

    private final int SOCKET_TIMEOUT = 630000;
    private final int POLL_TIME = 2;
    private final int WAIT_TIME = 570;

    /**
     * Calls an api with GET verb. This method will ONLY get translated parts ie. componentType = Parts/Assemblies
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState, not_costed". The operand (eg. [CN]) MUST be included in the query.
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     */
    public List<ScenarioItem> getComponentParts(UserCredentials userCredentials, String... paramKeysValues) {

        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                List<ScenarioItem> scenarioItemList = getBaseCssComponents(userCredentials, paramKeysValues);

                if (scenarioItemList.size() > 0 &&

                    scenarioItemList.stream()
                        .noneMatch(o -> o.getComponentType().equalsIgnoreCase("unknown")) &&

                    scenarioItemList.stream()
                        .allMatch(o -> ScenarioStateEnum.terminalState.stream()
                            .anyMatch(x -> x.getState().equalsIgnoreCase(o.getScenarioState())))) {

                    return scenarioItemList;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new RuntimeException(String.format("Failed to get uploaded component after %d seconds", WAIT_TIME));
    }

    /**
     * Calls an api with GET verb
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param userCredentials - the user credentials
     * @return response object
     */
    public ScenarioItem findFirst(String componentName, String scenarioName, UserCredentials userCredentials) {
        return getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName.toUpperCase(), SCENARIO_NAME_EQ.getKey() + scenarioName)
            .stream()
            .findFirst()
            .orElse(null);
    }

    /**
     * Calls an api with GET verb. No wait is performed for this call.
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState[EQ], not_costed". The operand (eg. [CN]) MUST be included in the query.
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     * @throws ArrayIndexOutOfBoundsException if only one of the key/value is supplied eg. "scenarioState" rather than "scenarioState[EQ], not_costed"
     */
    public List<ScenarioItem> getBaseCssComponents(UserCredentials userCredentials, String... paramKeysValues) {
        return getBaseCssComponents(userCredentials,
            new KeyValueUtil().keyValue(paramKeysValues, ","))
            .getResponseEntity()
            .getItems();
    }

    /**
     * Calls an api with GET verb
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState[EQ], not_costed". The operand (eg. [CN]) MUST be included in the query.
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     */
    public List<ScenarioItem> getWaitBaseCssComponents(UserCredentials userCredentials, String... paramKeysValues) {

        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                List<ScenarioItem> scenarioItemList = getBaseCssComponents(userCredentials, paramKeysValues);

                if (scenarioItemList.size() > 0 &&

                    scenarioItemList.stream()
                        .allMatch(o -> ScenarioStateEnum.terminalState.stream()
                            .anyMatch(x -> x.getState().equalsIgnoreCase(o.getScenarioState())))) {

                    return scenarioItemList;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new RuntimeException(String.format("Failed to get uploaded component after %d seconds", WAIT_TIME));
    }

    /**
     * Calls an api with GET verb
     *
     * @return the response wrapper that contains the response data
     */
    private ResponseWrapper<ComponentResponse> getBaseCssComponents(UserCredentials userCredentials, QueryParams queryParams) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, ComponentResponse.class)
            .queryParams(queryParams)
            .token(userCredentials.getToken())
            .socketTimeout(SOCKET_TIMEOUT)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Creates search request by component type
     *
     * @param userCredentials - the user credentials
     * @param componentType   - the component type
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<ComponentResponse> postSearchRequest(UserCredentials userCredentials, String componentType) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS_SEARCH, ComponentResponse.class)
            .token(userCredentials.getToken())
            .headers(new HashMap<>() {
                {
                    put("content-type", "application/x-www-form-urlencoded");
                }
            }).xwwwwFormUrlEncodeds(Collections.singletonList(new HashMap<String, String>() {
                {
                    put("pageNumber", "1");
                    put("pageSize", "10");
                    put("latest[EQ]", "true");
                    put("scenarioPublished[EQ]", "true");
                    put("componentType[IN]", componentType);
                    put("sortBy[DESC]", "scenarioCreatedAt");
                }
            })).expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets request of common iteration endpoint
     *
     * @param userCredentials - the user credentials
     * @return The response wrapper that contains the response data.
     */
    public ResponseWrapper<ComponentResponse> getIterationsRequest(UserCredentials userCredentials) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, ComponentResponse.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .token(userCredentials.getToken());
        return HTTPRequest.build(requestEntity).get();
    }
}
