package com.apriori.apibase.utils;

import com.apriori.apibase.services.ats.objects.Token;
import com.apriori.apibase.services.ats.objects.TokenInformation;
import com.apriori.apibase.services.ats.objects.TokenRequest;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

public class JwtTokenUtil {

    public String retrieveJwtToken(String secretKey, String url, int statusCode, String username, String email, String issuer, String subject) {
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
}
