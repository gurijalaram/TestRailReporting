package com.apriori.utils.token;

import com.apriori.utils.enums.TokenEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {
    private String username = PropertiesContext.get("${env}.ats.token_username");
    private String email = PropertiesContext.get("${env}.ats.token_email");
    private String issuer = PropertiesContext.get("${env}.ats.token_issuer");
    private String subject = PropertiesContext.get("${customer}.token_subject");

    public TokenUtil(UserCredentials userCredentials) {
        this.username = userCredentials.getUsername();
        this.email = userCredentials.getEmail();
    }

    public TokenUtil(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public TokenUtil() {
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public ResponseWrapper<Token> getToken() {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = RequestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .claims(Claims.builder()
                        .name(username)
                        .email(email)
                        .build())
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets token as string
     *
     * @return string
     */
    public String getTokenAsString() {
        return getToken().getResponseEntity().getToken();
    }
}
