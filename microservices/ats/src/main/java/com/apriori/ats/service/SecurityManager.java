package com.apriori.ats.service;

import com.apriori.ats.entity.request.AuthorizeRequest;
import com.apriori.ats.entity.request.Token;
import com.apriori.ats.entity.request.TokenRequest;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.entity.response.TokenInformation;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

public class SecurityManager {
    public static String retrieveJwtToken(String secretKey, String url, int statusCode, String username, String email, String issuer, String subject) {
        url = "https://" + url;
        url = url.concat(String.format("/tokens?key=%s", secretKey));
        TokenRequest body = new TokenRequest();
        TokenInformation information = new TokenInformation();
        information
            .setIssuer(issuer)
            .setSubject(subject)
            .setNameAndEmail(username, email);
        body.setToken(information);

        Token token = (Token) GenericRequestUtil.postMultipart(
            RequestEntity.init(url, Token.class)
                .setBody(body)
                .setStatusCode(statusCode),
            new RequestAreaApi()
        ).getResponseEntity();

        return token.getToken();
    }

    public static AuthorizationResponse authorizeUser(String secretKey, String url, String targetCloudContext,
                                                      String token, int statusCode) {
        url = "https://" + url;
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
