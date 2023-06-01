package com.apriori.gcd.controller;

import com.apriori.gcd.entity.enums.GCDAPIEnum;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

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
