package com.apriori.dfs.models.utils;

import com.apriori.dfs.enums.DFSApiEnum;
import com.apriori.dfs.models.response.DigitalFactories;
import com.apriori.dfs.models.response.DigitalFactory;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

import java.util.List;

public class DigitalFactoryUtil {

    /**
     * GET digital factories
     *
     * @return user object
     */
    public ResponseWrapper<DigitalFactories> getDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class);

        ResponseWrapper<DigitalFactories> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse;
    }

    /**
     * POST digital factories
     *
     * @return user object
     */
    public List<DigitalFactory> postDigitalFactories() {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class);

        ResponseWrapper<DigitalFactories> userResponse = HTTPRequest.build(requestEntity).post();
        return userResponse.getResponseEntity().getItems();
    }

    /**
     * GET digital factories by identity
     *
     * @return user object
     */
    public List<DigitalFactory> getDigitalFactoriesIdentity(String identity) {
        final RequestEntity requestEntity = RequestEntityUtil.init(DFSApiEnum.DIGITAL_FACTORIES, DigitalFactories.class)
            .inlineVariables(identity);

        ResponseWrapper<DigitalFactories> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity().getItems();
    }
}
