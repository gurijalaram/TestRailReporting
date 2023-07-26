package com.apriori.ats.utils;

import com.apriori.ats.entity.request.AuthorizeRequest;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.enums.AuthorizeUserEnum;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

public class AuthorizeUserUtil {

    /**
     * POST authorize user
     *
     * @param targetCloudContext - target cloud context
     * @param token              - token
     * @return authorization object
     */
    public static ResponseWrapper<AuthorizationResponse> authorizeUser(String targetCloudContext, String token) {

        final RequestEntity requestEntity = RequestEntityUtil.init(AuthorizeUserEnum.POST_AUTHORIZE_BY_BASE_URL_SECRET, AuthorizationResponse.class)
            .body(AuthorizeRequest.builder().targetCloudContext(targetCloudContext)
                .token(token)
                .build());

        return HTTPRequest.build(requestEntity).post();
    }
}
