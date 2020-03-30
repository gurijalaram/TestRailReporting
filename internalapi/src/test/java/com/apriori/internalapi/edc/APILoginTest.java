package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.apibase.http.builder.common.response.common.AccountStatus;
import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.common.response.common.ErrorRequestResponse;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.builder.service.RequestAreaUiAuth;
import com.apriori.apibase.http.enums.common.api.AccountEndpointEnum;
import com.apriori.apibase.http.enums.common.api.AuthEndpointEnum;
import com.apriori.apibase.utils.ResponseWrapper;
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
