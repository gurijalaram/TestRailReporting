package com.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.ats.objects.Token;
import com.apriori.apibase.services.ats.objects.TokenInformation;
import com.apriori.apibase.services.ats.objects.TokenRequest;
import com.apriori.apibase.services.cas.objects.Customers;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.HashMap;

public class CasCustomersTest extends TestUtil {

    private static String token;

    @Test
    @Description("Get a list of CAS customers")
    public void getCustomers() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers?sortBy[ASC]=name");

        token = retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCasServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCasTokenUsername(),
            Constants.getCasTokenEmail(),
            Constants.getCasTokenIssuer(),
            Constants.getCasTokenSubject());

        ResponseWrapper<Customers> response = getCommonRequest(apiUrl, "cfrith@apriori.com", "TestEvent2022!", Customers.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    // TODO: 22/01/2021 (ciene) to be refactored
    private HashMap<String, String> initAuthorizationHeaderNoContent() {
        return new HashMap<String, String>() {{
            put("Authorization", "Bearer " + token);
            put("Content-Type", "application/json");
        }};
    }

    // TODO: 22/01/2021 (ciene) to be refactored
    private  <T> ResponseWrapper<T> getCommonRequest(String url, String username, String password, Class klass) {
        return GenericRequestUtil.get(
            RequestEntity.init(url, UserCredentials.init(username, password), klass)
                .setHeaders(initAuthorizationHeaderNoContent()), new RequestAreaApi()
        );
    }

    // TODO: 22/01/2021 (ciene) to be refactored
    private static String retrieveJwtToken(String secretKey, String url, int statusCode, String username, String email, String issuer, String subject) {
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
}
