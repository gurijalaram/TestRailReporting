package com.apriori.ats.utils;

import com.apriori.apibase.services.ats.objects.Token;
import com.apriori.apibase.services.ats.objects.TokenInformation;
import com.apriori.apibase.services.ats.objects.TokenRequest;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class JwtTokenUtil {

    private String username = Constants.getAtsTokenUsername();
    private String email = Constants.getAtsTokenEmail();
    private String apiUrl = Constants.getAtsServiceHost();
    private String secretKey = Constants.getSecretKey();
    private String issuer = Constants.getAtsTokenIssuer();
    private String subject = Constants.getAtsTokenSubject();
    private String currentToken;

    public JwtTokenUtil(UserCredentials userCredentials) {
        this.username = userCredentials.getUsername().split("@")[0];
        this.email = userCredentials.getUsername();
    }

    public JwtTokenUtil() {
    }

    /**
     * Retrieves a JWT token
     *
     * @return string
     */
    public String retrieveJwtToken() {
        if (currentToken != null) {
            return currentToken;
        }

        log.info("Retrieving JWT Token...");

        String url = apiUrl.concat(String.format("/tokens?key=%s", secretKey));
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
                .setStatusCode(HttpStatus.SC_CREATED),
            new RequestAreaApi()
        ).getResponseEntity();

        return currentToken = token.getToken();
    }
}
