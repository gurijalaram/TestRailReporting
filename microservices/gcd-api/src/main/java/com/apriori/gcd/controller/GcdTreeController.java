package com.apriori.gcd.controller;

import com.apriori.gcd.entity.enums.GCDAPIEnum;
import com.apriori.gcd.entity.response.GcdTree;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

public class GcdTreeController extends RequestEntityUtil {

    /**
     * Post a GCD tree
     *
     * @param gcdTree     - the gcd tree
     * @param currentUser - the current user
     * @return ResponseWrapper <GcdTree>
     */
    public GcdTree postGcdTree(String gcdTree, UserCredentials currentUser) {
        final RequestEntity requestEntity = init(GCDAPIEnum.TREE_DIFF, GcdTree.class)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .customBody(gcdTree)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<GcdTree> gcdTreeResponse = HTTPRequest.build(requestEntity).post();
        return gcdTreeResponse.getResponseEntity();
    }
}
