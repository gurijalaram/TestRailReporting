package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.enums.common.api.AuthEndpointEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;

import org.junit.Test;

public class APILoginTest {

    @Test
    @Description("Test auto login functionality")
    public void testTokenAutoLoginFiledIfIncorrectLoginProcess() {
        final UserCredentials userCredentials = UserUtil.getUser();
        new HTTPRequest().defaultFormAuthorization(userCredentials.getUsername(), userCredentials.getPassword())
            .customizeRequest()
            .setEndpoint(AuthEndpointEnum.POST_AUTH)
            .commitChanges()
            .connect()
            .post();
    }

    @Test
    @Description("Test default login to environment")
    public void testDefaultLoginFiledIfIncorrectLoginProcess() {
        final UserCredentials userCredentials = UserUtil.getUser();
        new HTTPRequest().defaultFormAuthorization(userCredentials.getUsername(), userCredentials.getPassword())
            .customizeRequest()
            .setEndpoint(AuthEndpointEnum.POST_AUTH)
            .setAutoLogin(false)
            .setReturnType(AuthenticateJSON.class)
            .commitChanges()
            .connect()
            .post();
    }

    @Test
    @Description("Test common login to environment")
    public void testLoginFiledIfIncorrectUserData() {
        AuthenticateJSON authenticateJSON = (AuthenticateJSON)
            new HTTPRequest().customFormAuthorization(this.initUserConnectionData("admin@apriori.com", "admin"))
                .customizeRequest()
                .setEndpoint(AuthEndpointEnum.POST_AUTH)
                .setAutoLogin(false)
                .setReturnType(AuthenticateJSON.class)
                .commitChanges()
                .connect()
                .post();

    }

    public UserAuthenticationEntity initUserConnectionData(final String username, final String password) {
        return new UserAuthenticationEntity(
            username,
            password,
            null,
            "password",
            "apriori-web-cost",
            "donotusethiskey",
            "tenantGroup%3Ddefault%20tenant%3Ddefault",
            false
        );
    }
}
