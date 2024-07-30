package com.apriori.gcd.api.controller;

import com.apriori.gcd.api.models.enums.GCDAPIEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class GcdTreeController {
    private RequestEntityUtil requestEntityUtil;

    public GcdTreeController(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Post a GCD tree
     *
     * @param gcdTree     - the gcd tree
     * @return a generic response object
     */
    public <T> ResponseWrapper<T> postGcdTree(String gcdTree, int expectedResponseCode, Class<T> klass) {
        final RequestEntity requestEntity = requestEntityUtil.init(GCDAPIEnum.TREE_DIFF, klass)
            .customBody(gcdTree)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).post();
    }
}
