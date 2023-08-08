package com.apriori.gcd.controller;

import com.apriori.gcd.models.enums.GCDAPIEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;

public class GcdTreeController extends RequestEntityUtil {

    /**
     * Post a GCD tree
     *
     * @param gcdTree     - the gcd tree
     * @param currentUser - the current user
     * @return a generic response object
     */
    public <T> ResponseWrapper<T> postGcdTree(String gcdTree, UserCredentials currentUser, int expectedResponseCode, Class<T> klass) {
        final RequestEntity requestEntity = init(GCDAPIEnum.TREE_DIFF, klass)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .customBody(gcdTree)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).post();
    }
}
