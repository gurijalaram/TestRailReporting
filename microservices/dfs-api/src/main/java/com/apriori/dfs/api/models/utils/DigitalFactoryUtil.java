package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.interfaces.EndpointEnum;

public class DigitalFactoryUtil {

    /**
     * FIND digital factories
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param inlineVariables - secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> findDigitalFactories(Integer expectedResponseCode,
                                                       Class<T> expectedType,
                                                       String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 0
            ? DFSApiEnum.DIGITAL_FACTORIES : DFSApiEnum.DIGITAL_FACTORIES_WITH_KEY_PARAM;

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories page
     *
     * @param endpoint - Target endpoint
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @return Response object
     */
    public <T> ResponseWrapper<T> findDigitalFactoriesPage(EndpointEnum endpoint,
                                                        Integer expectedResponseCode,
                                                        Class<T> expectedType,
                                                        String... inlineVariables) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(endpoint, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET digital factory
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param inlineVariables - identity or identity/secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> getDigitalFactory(Integer expectedResponseCode,
                                                    Class<T> expectedType,
                                                    String... inlineVariables) {

        return HTTPRequest.build(getRequestEntity(expectedResponseCode, expectedType, inlineVariables)).get();
    }

    /**
     * GET digital factory
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType Expected type from body of HTTP response
     * @param identity - identity
     * @return Response object
     */
    public <T> ResponseWrapper<T> getDigitalFactoryWithoutKeyParameter(Integer expectedResponseCode,
                                                                       Class<T> expectedType,
                                                                       String identity) {

        final RequestEntity requestEntity =  RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH, expectedType)
            .inlineVariables(new String[]{ identity, ""}) // hack - add one more empty variable to skip auto adding shared secret
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }


    /**
     * Update or Insert a DigitalFactory
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param requestBody - Request body
     * @return Response object
     */
    public <T> ResponseWrapper<T> upsertDigitalFactory(Integer expectedResponseCode,
                                                       Class<T> expectedType,
                                                       Object requestBody) {

        return upsertDigitalFactory(expectedResponseCode, expectedType, requestBody, null);
    }

    /**
     * Update or Insert a DigitalFactory
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param requestBody - Request body
     * @param contentType - Content type
     * @return Response object
     */
    public <T> ResponseWrapper<T> upsertDigitalFactory(Integer expectedResponseCode,
                                                       Class<T> expectedType,
                                                       Object requestBody,
                                                       String contentType,
                                                       String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 0
            ? DFSApiEnum.DIGITAL_FACTORIES : DFSApiEnum.DIGITAL_FACTORIES_WITH_KEY_PARAM;

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(path, expectedType)
                .body(requestBody)
                .inlineVariables(inlineVariables)
                .expectedResponseCode(expectedResponseCode);

        if (contentType != null) {
            requestEntity.headers().put("Content-Type", contentType);
        }

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * DELETE digital factory by identity
     *
     * @param expectedResponseCode - Expected HTTP status code
     * @param expectedType - Expected type from body of HTTP response
     * @param inlineVariables - identity or identity/secret
     * @return Response object
     */
    public <T> ResponseWrapper<T> deleteDigitalFactory(Integer expectedResponseCode,
                                                       Class<T> expectedType,
                                                       String... inlineVariables) {

        return HTTPRequest.build(getRequestEntity(expectedResponseCode, expectedType, inlineVariables)).delete();
    }

    private <T> RequestEntity getRequestEntity(Integer expectedResponseCode,
                                               Class<T> expectedType,
                                               String... inlineVariables) {

        DFSApiEnum path = inlineVariables.length == 1
            ? DFSApiEnum.DIGITAL_FACTORIES_BY_PATH : DFSApiEnum.DIGITAL_FACTORIES_BY_PATH_WITH_KEY_PARAM;

        return RequestEntityUtil_Old.init(path, expectedType)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);
    }
}
