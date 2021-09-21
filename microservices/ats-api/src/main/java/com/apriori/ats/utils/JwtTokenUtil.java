package com.apriori.ats.utils;

import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.apibase.services.ats.objects.Token;
import com.apriori.apibase.services.ats.objects.TokenInformation;
import com.apriori.apibase.services.ats.objects.TokenRequest;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenUtil {

    private String currentToken;
    private String username = PropertiesContext.get("${env}.ats.token_username");
    private String email = PropertiesContext.get("${env}.ats.token_email");
    private String issuer = PropertiesContext.get("${env}.ats.token_issuer");
    private String subject = PropertiesContext.get("${customer}.token_subject");

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

        TokenRequest body = new TokenRequest();
        TokenInformation information = new TokenInformation();
        information
            .setIssuer(issuer)
            .setSubject(subject)
            .setNameAndEmail(username, email);
        body.setToken(information);

        RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.POST_TOKEN, Token.class)
            .body(body);

        Token token = (Token) HTTP2Request.build(requestEntity)
            .postMultipart()
            .getResponseEntity();

        return currentToken = token.getToken();
    }
}
