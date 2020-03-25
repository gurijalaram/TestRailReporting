package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIValue {

    private static final Logger logger = LoggerFactory.getLogger(APIValue.class);

    /**
     * Queries API endpoint to get value of the path
     *
     * @param username - logged in user username
     * @param apiPath  - the field
     * @return string
     */
    public String getValueFromAPI(String username, String endpoint, Class entityClass, String apiPath) {

        String jsonResponse = new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(new APIAuthentication().initAuthorizationHeader(username))
            .setEndpoint(Constants.getBaseUrl() + endpoint)
            .setAutoLogin(false)
            .setReturnType(entityClass)
            .commitChanges()
            .connect()
            .get()
            .getBody();

        JsonNode node = null;
        try {
            node = new ObjectMapper().readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
        }

        return node.findPath(apiPath).asText();
    }
}
