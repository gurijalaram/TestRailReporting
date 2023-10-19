package com.apriori.dfs.models.utils;

import com.apriori.dfs.enums.DFSApiEnum;
import com.apriori.dfs.models.response.DigitalFactories;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ErrorMessage;

import software.amazon.awssdk.http.HttpStatusCode;

public class DigitalFactoryUtil {

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
     * GET digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> getDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .expectedResponseCode(HttpStatusCode.OK);

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
