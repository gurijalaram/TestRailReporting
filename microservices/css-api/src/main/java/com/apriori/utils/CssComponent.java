package com.apriori.utils;

import static com.apriori.css.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
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

                List<ScenarioItem> scenarioItemList = getBaseCssComponents(userCredentials, paramKeysValues).getResponseEntity().getItems();

                if (scenarioItemList.size() > 0 &&

                    scenarioItemList.stream()
                        .noneMatch(o -> o.getComponentType().equalsIgnoreCase("unknown")) &&

                    scenarioItemList.stream()
                        .allMatch(o -> ScenarioStateEnum.terminalState.stream()
                            .anyMatch(x -> x.getState().equalsIgnoreCase(o.getScenarioState())))) {

                    // TODO: 04/11/2022 change getbasecss to return list<scenarioItem>
                    return scenarioItemList;
                }

            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(String.format("Failed to get uploaded component after %d seconds", WAIT_TIME));
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
     * Calls an api with GET verb
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState[EQ], not_costed". The operand (eg. [CN]) MUST be included in the query.
     * @param userCredentials - the user credentials
     * @return the response wrapper that contains the response data
     * @throws ArrayIndexOutOfBoundsException if only one of the paramKeysValues is supplied eg. "scenarioState" rather than "scenarioState, not_costed"
     */
    public ResponseWrapper<CssComponentResponse> getBaseCssComponents(UserCredentials userCredentials, String... paramKeysValues) {
        QueryParams queryParams = new QueryParams();

        List<String[]> paramKeyValue = Arrays.stream(paramKeysValues).map(o -> o.split(",")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        // TODO: 26/10/2022 cn - wrap in a try/catch/throw, when incorrect k,v is entered its easily debuggable
        paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));

        return getBaseCssComponents(userCredentials, queryParams.use(paramMap));
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

                List<ScenarioItem> scenarioItemList = getBaseCssComponents(userCredentials, paramKeysValues).getResponseEntity().getItems();

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
        throw new IllegalArgumentException(String.format("Failed to get uploaded component after %d seconds", WAIT_TIME));
    }

    /**
     * Calls an api with GET verb
     *
     * @return the response wrapper that contains the response data
     */
    private ResponseWrapper<CssComponentResponse> getBaseCssComponents(UserCredentials userCredentials, QueryParams queryParams) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, CssComponentResponse.class)
            .queryParams(queryParams)
            .token(userCredentials.getToken())
            .socketTimeout(SOCKET_TIMEOUT)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }
}
