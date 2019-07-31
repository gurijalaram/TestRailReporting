package main.java.api;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.enums.UsersEnum;
import main.java.http.builder.common.entity.UserAuthenticationEntity;
import main.java.http.builder.common.response.common.AuthenticateJSON;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.AuthEndpointEnum;
import org.junit.Assert;
import org.junit.Test;

public class APILoginTest {



    @Test
    @Description("Test auto login functionality")
    @Severity(SeverityLevel.NORMAL)
    public void testTokenAutoLoginFiledIfIncorrectLoginProcess() {
        new HTTPRequest().defaultFormAuthorization(UsersEnum.ADMIN_DEFAULT_USER)
                .customizeRequest()
                .setEndpoint(AuthEndpointEnum.POST_AUTH)
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Test default login to environment")
    @Severity(SeverityLevel.NORMAL)
    public void testDefaultLoginFiledIfIncorrectLoginProcess() {
        new HTTPRequest().defaultFormAuthorization(UsersEnum.ADMIN_DEFAULT_USER)
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
    @Severity(SeverityLevel.NORMAL)
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
