package com.apriori.utils;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

@Slf4j
public class UncostedComponents {

    /**
     * Gets the uncosted component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getUnCostedCssComponent(String componentName, String scenarioName, String token) {
        return getCssComponent(componentName, scenarioName, token, "NOT_COSTED");
    }

    /**
     * Gets component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getUnCostedCssComponent(String componentName, String scenarioName) {
        return getCssComponent(componentName, scenarioName, new JwtTokenUtil().retrieveJwtToken(), "NOT_COSTED");
    }

    /**
     * Gets component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public List<Item> getCssComponent(String componentName, String scenarioName, String token, String verifiedState) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.GET_COMPONENT_BY_COMPONENT_SCENARIO_NAMES, CssComponentResponse.class)
            .inlineVariables(componentName.split("\\.")[0].toUpperCase(), scenarioName)
            .token(token);

        final int attemptsCount = 60;
        final int secondsToWait = 2;
        int currentCount = 0;

        try {

            do {
                TimeUnit.SECONDS.sleep(secondsToWait);

                ResponseWrapper<CssComponentResponse> scenarioRepresentation = HTTP2Request.build(requestEntity).get();

                Assert.assertEquals(String.format("Failed to receive data about component name: %s, scenario name: %s, status code: %s", componentName, scenarioName, scenarioRepresentation.getStatusCode()),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                final List<Item> items = scenarioRepresentation.getResponseEntity().getItems();

                if (!items.isEmpty()) {
                    final Item firstItem = scenarioRepresentation.getResponseEntity().getItems().get(0);

                    if (firstItem.getScenarioState().equals("PROCESSING_FAILED")) {
                        throw new RuntimeException(String.format("Processing has failed for component name: %s, scenario name: %s", componentName, scenarioName));
                    }

                    if (firstItem.getScenarioState().equals(verifiedState.toUpperCase())) {
                        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                        return scenarioRepresentation.getResponseEntity().getItems().stream().filter(x -> !x.getComponentType().equals("UNKNOWN")).collect(Collectors.toList());
                    }
                }
            } while (currentCount++ <= attemptsCount);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d attempts with period in %d seconds.",
                componentName, scenarioName, attemptsCount, secondsToWait)
        );
    }
}
