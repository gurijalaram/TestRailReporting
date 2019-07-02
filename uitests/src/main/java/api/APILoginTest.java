package main.java.api;

import main.java.common.HTTPRequest;
import main.java.common.UserForAPIConnection;
import main.java.enums.UsersEnum;
import main.java.enums.common.AuthEndpointEnum;
import main.java.enums.common.CommonEndpointEnum;
import main.java.enums.common.TemporaryAPIEnum;
import main.java.pojo.common.AuthenticateJSON;
import org.junit.Test;

public class APILoginTest {

    @Test
    public void testTokenAutoLoginFiledIfIncorrectLoginProcess() {
        new HTTPRequest().userEnum(UsersEnum.ADMIN_DEFAULT_USER)
                .endpoint(AuthEndpointEnum.POST_AUTH)
                .useAutoLogin(false)
                .followRedirection(false)
                .statusCode(200)
                .returnType(AuthenticateJSON.class)
                .connect()
                .post();
    }

    @Test
    public void testLoginFiledIfIncorrectUserData() {
        AuthenticateJSON authenticateJSON = (AuthenticateJSON)
                new HTTPRequest().customFormAuthorization(this.initUserConnectionData("admin@apriori.com", "admin"))
                .endpoint(AuthEndpointEnum.POST_AUTH)
                .useAutoLogin(false)
                .followRedirection(false)
                .statusCode(200)
                .returnType(AuthenticateJSON.class)
                .connect()
                .post();

    }

    @Test
    public void testAccountsStatusFiledIf() {
        new HTTPRequest().userEnum(UsersEnum.CIE_TE_USER)
                .endpoint(TemporaryAPIEnum.GET_ACCOUNTS_STATUS)
//                .endpoint("http://edc-api.atv.awsdev.apriori.com/accounts/status")
                .statusCode(200)
//                .returnType(AuthenticateJSON.class)
                .connect()
                .get();

    }

    public UserForAPIConnection initUserConnectionData(final String username, final String password) {
        return new UserForAPIConnection(
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
