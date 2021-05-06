package com.apriori.ats.utils;

import com.apriori.ats.entity.request.AuthorizeRequest;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

public class AuthorizeUserUtil {

    public static AuthorizationResponse authorizeUser(String secretKey, String url, String targetCloudContext,
                                                      String token, int statusCode) {
        url = url.concat(String.format("/authorize?key=%s", secretKey));
        AuthorizeRequest request = new AuthorizeRequest();

        AuthorizeRequest body = request.setTargetCloudContext(targetCloudContext).setToken(token);

        return (AuthorizationResponse) GenericRequestUtil.postMultipart(
            RequestEntity.init(url, AuthorizationResponse.class)
                .setBody(body)
                .setStatusCode(statusCode),
            new RequestAreaApi()
        ).getResponseEntity();
    }
}
