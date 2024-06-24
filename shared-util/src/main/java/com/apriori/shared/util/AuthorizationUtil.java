package com.apriori.shared.util;

import com.apriori.shared.util.enums.TokenEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.TokenRequest;
import com.apriori.shared.util.models.response.Claims;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.models.response.TokenInformation;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;

@Slf4j
public class AuthorizationUtil {

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public synchronized ResponseWrapper<Token> getToken(UserCredentials userCredentials) {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = RequestEntityUtil_Old.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(PropertiesContext.get("ats.token_issuer"))
                    .subject(SharedCustomerUtil.getTokenSubjectForCustomer())
                    .claims(Claims.builder()
                        .name(userCredentials.getUsername())
                        .email(userCredentials.getEmail())
                        .build())
                    .build())
                .build())
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }
}
