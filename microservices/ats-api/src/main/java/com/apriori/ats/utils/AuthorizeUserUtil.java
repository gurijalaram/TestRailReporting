package com.apriori.ats.utils;

import com.apriori.ats.entity.request.AuthorizeRequest;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.enums.AuthorizeUserEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class AuthorizeUserUtil {

    public static ResponseWrapper<AuthorizationResponse> authorizeUser(String targetCloudContext,                                                                       String token) {
        AuthorizeRequest authorizeRequest = new AuthorizeRequest().setTargetCloudContext(targetCloudContext).setToken(token);

        final RequestEntity requestEntity = RequestEntityUtil.init(AuthorizeUserEnum.POST_MULTIPART_AUTHORIZE_BY_BASE_URL_SECRET, AuthorizationResponse.class)
            .body(authorizeRequest);

        return HTTPRequest.build(requestEntity).postMultipart();
    }
}
