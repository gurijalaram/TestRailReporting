package com.apriori.apibase.utils;

import com.apriori.apibase.services.ats.objects.AuthorizationResponse;
import com.apriori.apibase.services.ats.objects.AuthorizeRequest;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

public class AuthorizeUserUtil {

    public AuthorizationResponse authorizeUser(String url, String secretKey, String application, String targetCloudContext,
                                                      String token, int statusCode) {
        url = "https://" + url;
        url = url.concat(String.format("/authorize?key=%s", secretKey));
        AuthorizeRequest request = new AuthorizeRequest();

        AuthorizeRequest body = request.setApplication(application).setTargetCloudContext(targetCloudContext).setToken(token);

        return (AuthorizationResponse) GenericRequestUtil.postMultipart(
            RequestEntity.init(url, AuthorizationResponse.class)
                .setBody(body)
                .setStatusCode(statusCode),
            new RequestAreaApi()
        ).getResponseEntity();
    }
}
