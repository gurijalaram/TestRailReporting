package main.java.api;

import main.java.ConnectionClass;
import main.java.ConnectionManager;
import main.java.UserForAPIConnection;
import main.java.enums.UsersEnum;
import main.java.enums.common.AuthEndpointEnum;
import main.java.pojo.common.AuthenticateJSON;
import static org.junit.Assert.*;
import org.junit.Test;

public class APILoginTest {

    @Test
    public void testLoginFiledIfIncorrectUserData() {

//                AuthenticateJSON authenticateJSON = new APIRuqest(driver).userAuthorizationData("username", "password")
//                .endpoint(AuthEndpointEnum.POST_AUTH)
//                .useAutoLogin(false)
//                .followRedirection(false)
//                .statusCode(200)
//                .returnType(AuthenticateJSON.class)
//                .connect();



//        AuthenticateJSON authenticateJSON=
        AuthenticateJSON authenticateJSON = (AuthenticateJSON) ConnectionManager.ConnectionManagerBuilder.fullUserDataBuild(new ConnectionClass(initUserConnectionData("admin@apriori.com", "admin")))
                .endpoint(AuthEndpointEnum.POST_AUTH)
                .useAutoLogin(false)
                .followRedirection(false)
                .statusCode(200)
                .returnType(AuthenticateJSON.class)
                .connect()
                .post();

        assertNotNull("access token should be present", authenticateJSON.getAccessToken());
        assertNotNull("Expires time should be present", authenticateJSON.getExpires_in());
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
