package com.apriori.ats.utils;

import com.apriori.apibase.services.ats.objects.Token;
import com.apriori.apibase.services.ats.objects.TokenInformation;
import com.apriori.apibase.services.ats.objects.TokenRequest;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class JwtTokenUtil {

    private String currentToken;
    private String username = PropertiesContext.get("${env}.ats.token_username");
    private String email = PropertiesContext.get("${env}.ats.token_email");
    private String apiUrl = PropertiesContext.get("${env}.ats.api_url");
    private String secretKey = PropertiesContext.get("${env}.secret_key");
    private String issuer = PropertiesContext.get("${env}.ats.token_issuer");
    private String subject = PropertiesContext.get("${env}.ats.token_subject");

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
