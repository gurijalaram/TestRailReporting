package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.dfs.api.models.response.DigitalFactory;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;

import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;

public class DigitalFactoryUtil {

    /**
     * GET digital factories
     *
     * @param expectedResponseCode - expected response code
     * @return user object
     */
    public ResponseWrapper<ErrorMessage> getDigitalFactories(Integer expectedResponseCode) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, ErrorMessage.class)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> findDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories with Invalid or without Shared Secret
     *
     * @param expectedResponseCode - expected response code
     * @return ErrorMessage Object
     */
    public ResponseWrapper<ErrorMessage> findDigitalFactoriesWithInvalidSharedSecret(Integer expectedResponseCode, String inlineVariables) {

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH_PARAMETER, ErrorMessage.class)
                .inlineVariables(inlineVariables)
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * POST digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> postDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET digital factories by identity
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> getDigitalFactoriesIdentity(String identity) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET digital factory by identity
     *
     * @return DigitalFactory object
     */
    public ResponseWrapper<DigitalFactory> getDigitalFactory(String identity) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH_PARAMETER, DigitalFactory.class)
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * GET digital factory by identity with Invalid or without Shared Secret
     *
     * @param expectedResponseCode - expected response code
     * @return ErrorMessage object
     */
    public ResponseWrapper<ErrorMessage> getDigitalFactory(Integer expectedResponseCode, String inlineVariables) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH_PARAMETER, ErrorMessage.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Update or Insert a DigitalFactory
     *
     * @return DigitalFactory object
     */
    public ResponseWrapper<DigitalFactory> upsertDigitalFactory(Map<String, Object> requestBody) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactory.class)
                .body(requestBody)
                .expectedResponseCode(HttpStatusCode.CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Update or Insert a DigitalFactory with Invalid Request
     *
     * @return ErrorMessage object
     */
    public ResponseWrapper<ErrorMessage> upsertDigitalFactoryWithInvalidRequest(Map<String, Object> requestBody, Integer expectedResponseCode, String inlineVariables, String contentType) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES_BY_PATH_PARAMETER, ErrorMessage.class)
                .body(requestBody)
                .inlineVariables(inlineVariables)
                .expectedResponseCode(expectedResponseCode);

        requestEntity.headers().put("Content-Type", contentType);

        return HTTPRequest.build(requestEntity).post();
    }
}
