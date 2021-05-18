package com.apriori.css.utils;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.enums.CssAPIEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class UncostedComponent {

    private static final Logger logger = LoggerFactory.getLogger(UncostedComponent.class);

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
        Constants.getCidServiceHost(),
        HttpStatus.SC_CREATED,
        Constants.getCidTokenUsername(),
        Constants.getCidTokenEmail(),
        Constants.getCidTokenIssuer(),
        Constants.getCidTokenSubject());

    static {
        RequestEntityUtil.useTokenForRequests(token);
    }

    /**
     * Gets the uncosted component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public ResponseWrapper<CssComponentResponse> getUncostedCssComponent(String componentName, String scenarioName) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.GET_COMPONENT_BY_COMPONENT_SCENARIO_NAMES, CssComponentResponse.class)
            .inlineVariables(Arrays.asList(componentName, scenarioName));

        long START_TIME = System.currentTimeMillis() / 1000;
        final long POLLING_INTERVAL = 5L;
        final long MAX_WAIT_TIME = 180L;
        String verifiedState = "NOT_COSTED";
        String scenarioState;
        ResponseWrapper<CssComponentResponse> scenarioRepresentation;

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        do {
            scenarioRepresentation = HTTP2Request.build(requestEntity).get();

            while (scenarioRepresentation.getResponseEntity().getResponse().getItems().isEmpty() && scenarioRepresentation.getResponseEntity().getResponse().getItems() == null) {
                scenarioRepresentation = HTTP2Request.build(requestEntity).get();
            }

            scenarioState = scenarioRepresentation.getResponseEntity().getResponse().getItems().get(0).getScenarioState();
            try {
                TimeUnit.SECONDS.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!scenarioState.equals(verifiedState.toUpperCase()) && ((System.currentTimeMillis() / 1000) - START_TIME) < MAX_WAIT_TIME);

        return scenarioRepresentation;
    }
}
