package com.apriori.utils;

import static com.apriori.utils.enums.ScenarioStateEnum.COSTING;
import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING;
import static com.apriori.utils.enums.ScenarioStateEnum.PROCESSING_FAILED;

import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cfrith
 */

@Slf4j
public class CssComponent {

    private List<String> itemScenarioState;

    /**
     * Gets the uncosted component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getUnCostedCssComponent(String componentName, String scenarioName, UserCredentials userCredentials) {
        return getCssComponent(componentName, scenarioName, userCredentials, ScenarioStateEnum.NOT_COSTED);
    }

    /**
     * Gets component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getUnCostedCssComponent(String componentName, String scenarioName) {
        // TODO: 12/01/2022 cn - UserUtil here needs to be reviewed before its used in sds tests
        return getCssComponent(componentName, scenarioName, UserUtil.getUser(), ScenarioStateEnum.NOT_COSTED);
    }

    /**
     * Gets component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getCssComponent(String componentName, String scenarioName, UserCredentials userCredentials, ScenarioStateEnum scenarioState) {
        final int SOCKET_TIMEOUT = 270000;

        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_NAME, CssComponentResponse.class)
            .inlineVariables(componentName.split("\\.")[0].toUpperCase(), scenarioName)
            .token(userCredentials.getToken())
            .socketTimeout(SOCKET_TIMEOUT);

        final int POLL_TIME = 2;
        final int WAIT_TIME = 240;
        final long START_TIME = System.currentTimeMillis() / 1000;

        try {
            do {
                TimeUnit.SECONDS.sleep(POLL_TIME);

                ResponseWrapper<CssComponentResponse> scenarioRepresentation = HTTPRequest.build(requestEntity).get();

                Assert.assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final Optional<List<Item>> items = Optional.of(scenarioRepresentation.getResponseEntity().getItems());

                if (items.get().size() > 0) {

                    Supplier<Stream<Item>> distinctItem = () -> items.get().stream().distinct();

                    distinctItem.get()
                        .filter(x -> x.getScenarioState().equals(PROCESSING_FAILED.getState()) && !scenarioState.getState().equals(PROCESSING_FAILED.getState()))
                        .findAny()
                        .ifPresent(y -> {
                            throw new RuntimeException(String.format("Processing has failed for component name: %s, scenario name: %s", componentName, scenarioName));
                        });

                    if (distinctItem.get()
                        .anyMatch(x -> x.getScenarioState().equals(scenarioState.getState()))) {

                        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                        return scenarioRepresentation.getResponseEntity().getItems();
                    }

                    if (distinctItem.get()
                        .noneMatch(x -> x.getScenarioState().equals(scenarioState.getState()) ||
                            x.getScenarioState().equals(PROCESSING.getState()) ||
                            x.getScenarioState().equals(COSTING.getState()))) {

                        itemScenarioState = items.get().stream().map(Item::getScenarioState).distinct().collect(Collectors.toList());
                        break;
                    }
                }
            } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d seconds. \n Expected: %s \n Found: %s",
                componentName, scenarioName, WAIT_TIME, scenarioState.getState(), itemScenarioState)
        );
    }
}