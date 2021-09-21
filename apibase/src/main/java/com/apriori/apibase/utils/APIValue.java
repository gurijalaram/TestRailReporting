package com.apriori.apibase.utils;

import com.apriori.apibase.enums.BaseAPIEnum;
import com.apriori.apibase.services.response.objects.ToleranceValuesEntity;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO ALL: test it
public class APIValue {

    private static final Logger logger = LoggerFactory.getLogger(APIValue.class);

    /**
     * Gets value from tolerance api
     *
     * @param username - the username
     * @param path     - the path
     * @return string representation of the api value
     */
    public String getToleranceValueFromEndpoint(String username, String path) {
        return getValueFromAPI(username, path);
    }

    /**
     * Queries API endpoint to get value of the path
     *
     * @param username    - logged in user username
     * @param apiPath     - the field
     * @return string representation of the api value
     */
    private String getValueFromAPI(String username, String apiPath) {
        RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.GET_TOLERANCE_VALUE, ToleranceValuesEntity.class)
            .headers(new APIAuthentication().initAuthorizationHeader(username));

        String jsonResponse = HTTP2Request.build(requestEntity).get().getBody();

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