package com.apriori.dfs.api.models.utils;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ErrorMessage;
import software.amazon.awssdk.http.HttpStatusCode;

public class DigitalFactoryUtil {
    private static final String INVALID_SHARED_SECRET_ENDPOINT = "?key=InvalidSharedSecret";

    /**
     * GET digital factories
     *
     * @param expectedResponseCode - expected response code
     * @return user object
     */
    public ResponseWrapper<ErrorMessage> getDigitalFactories(Integer expectedResponseCode) {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, ErrorMessage.class)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> findDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories with Invalid Shared Secret
     *
     * @param expectedResponseCode - expected response code
     * @return ErrorMessage Object
     */
    public ResponseWrapper<ErrorMessage> findDigitalFactoriesWithInvalidSharedSecret(Integer expectedResponseCode) {

        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES_INVALID_SHARED_SECRET, ErrorMessage.class)
                .inlineVariables(INVALID_SHARED_SECRET_ENDPOINT)
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * FIND digital factories without Shared Secret
     *
     * @param expectedResponseCode - expected response code
     * @return ErrorMessage Object
     */
    public ResponseWrapper<ErrorMessage> findDigitalFactoriesWithoutSharedSecret(Integer expectedResponseCode) {

        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES_INVALID_SHARED_SECRET, ErrorMessage.class)
                .inlineVariables("")
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * POST digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> postDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * GET digital factories by identity
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> getDigitalFactoriesIdentity(String identity) {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .inlineVariables(identity)
            .expectedResponseCode(HttpStatusCode.OK);

        return HTTPRequest.build(requestEntity).get();
    }
}
