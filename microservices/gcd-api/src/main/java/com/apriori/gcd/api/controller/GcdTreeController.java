package com.apriori.gcd.api.controller;

import com.apriori.gcd.api.models.enums.GCDAPIEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

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
