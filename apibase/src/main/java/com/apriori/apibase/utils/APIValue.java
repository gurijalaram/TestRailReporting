package com.apriori.apibase.utils;

import com.apriori.apibase.services.response.objects.ToleranceValuesEntity;
import com.apriori.utils.http.builder.service.HTTPRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIValue {

    private static final Logger logger = LoggerFactory.getLogger(APIValue.class);

    private String baseURL = System.getProperty("baseURL");

    /**
     * Gets value from tolerance api
     *
     * @param username - the username
     * @param path     - the path
     * @return string representation of the api value
     */
    public String getToleranceValueFromEndpoint(String username, String path) {
        return getValueFromAPI(username, "ws/workspace/users/me/tolerance-policy-defaults", ToleranceValuesEntity.class, path);
    }

    /**
     * Queries API endpoint to get value of the path
     *
     * @param username    - logged in user username
     * @param endpoint    - the end point
     * @param entityClass - the entity class
     * @param apiPath     - the field
     * @return string representation of the api value
     */
    private String getValueFromAPI(String username, String endpoint, Class entityClass, String apiPath) {

        String jsonResponse = new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(new APIAuthentication().initAuthorizationHeader(username))
            .setEndpoint(baseURL + endpoint)
            .setAutoLogin(false)
            .setReturnType(entityClass)
            .commitChanges()
            .connect()
            .get()
            .getBody();

        JsonNode node;
        try {
            node = new ObjectMapper().readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("can't read json node");
        }

        return node.findPath(apiPath).asText();
    }
}