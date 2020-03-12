package com.apriori.apibase.services.utils;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.objects.AuthorizationResponse;
import com.apriori.apibase.services.objects.AuthorizeRequest;
import com.apriori.apibase.services.objects.Token;
import com.apriori.apibase.services.objects.TokenInformation;
import com.apriori.apibase.services.objects.TokenRequest;
import com.apriori.utils.constants.Constants;

public class SecurityManager {
    public static String retriveJwtToken(String url, int statusCode, String username, String email, String issuer, String subject) {
        url = "https://" + url;
        url = url.concat(String.format("/tokens?key=%s", Constants.getSecretKey()));
        TokenRequest body = new TokenRequest();
        TokenInformation information = new TokenInformation();
        information
                .setIssuer(issuer)
                .setSubject(subject)
                .setNameAndEmail(username, email);
        body.setToken(information);
        Token token = (Token) ServiceConnector.postToService(url, Token.class, body, statusCode);
        return token.getToken();
    }

    public static AuthorizationResponse authorizeUser(String url, String application, String targetCloudContext,
                                                      String token, int statusCode) {
        url = "https://" + url;
        url = url.concat(String.format("/authorize?key=%s", Constants.getSecretKey()));
        AuthorizeRequest request = new AuthorizeRequest();
        AuthorizeRequest body = request.setApplication(application).setTargetCloudContext(targetCloudContext).setToken(token);
        return (AuthorizationResponse)ServiceConnector.postToService(url, AuthorizationResponse.class, body, statusCode);
    }
}
