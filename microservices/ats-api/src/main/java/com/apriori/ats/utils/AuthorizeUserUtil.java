package com.apriori.ats.utils;

import com.apriori.ats.entity.request.AuthorizeRequest;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.enums.AuthorizeUserEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.junit.Assert;

public class AuthorizeUserUtil {

    public static AuthorizationResponse authorizeUser(String secretKey, String url, String targetCloudContext,
                                                      String token, int statusCode) {
        AuthorizeRequest authorizeRequest = new AuthorizeRequest().setTargetCloudContext(targetCloudContext).setToken(token);

        final RequestEntity requestEntity = RequestEntityUtil.init(AuthorizeUserEnum.POST_MULTIPART_AUTHORIZE_BY_BASE_URL_SECRET, AuthorizationResponse.class)
            .inlineVariables(url, secretKey)
            .body(authorizeRequest);

        ResponseWrapper<AuthorizationResponse> responseWrapper = HTTP2Request.build(requestEntity).postMultipart();
        Assert.assertEquals(statusCode, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity();
    }
}
