package com.apriori.edc.tests;

import com.apriori.apibase.services.response.objects.AccountStatus;
import com.apriori.apibase.services.response.objects.ErrorRequestResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.HTTPRequest;
import com.apriori.utils.http.builder.service.RequestAreaUiAuth;
import com.apriori.utils.http.enums.common.api.AccountEndpointEnum;
import com.apriori.utils.http.enums.common.api.AuthEndpointEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.junit.Test;

public class APILoginTest {


    @Test
    @Description("Test get accounts")
    @Severity(SeverityLevel.NORMAL)
    public void testGetWrappedAccounts() {
        final UserCredentials userCredentials = UserUtil.getUser();

        new HTTPRequest().defaultFormAuthorization(userCredentials.getUsername(), userCredentials.getPassword())
                .customizeRequest()
                .setEndpoint(AccountEndpointEnum.GET_ACCOUNTS)
                .setReturnType(AccountStatus.class)
                .commitChanges()
                .connect()
                .get();
    }

    @Test
    @Description("Test auto login functionality")
    public void testTokenAutoLoginFiledIfIncorrectLoginProcess() {
        ResponseWrapper<ErrorRequestResponse> accountStatusResponseWrapper =
                GenericRequestUtil.get(RequestEntity.init(AuthEndpointEnum.POST_AUTH, UserUtil.getUser(), ErrorRequestResponse.class),
                        new RequestAreaUiAuth()
                );
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
