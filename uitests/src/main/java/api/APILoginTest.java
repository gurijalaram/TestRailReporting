package main.java.api;

import main.java.common.ConnectionClass;
import main.java.common.UserForAPIConnection;
import main.java.enums.common.AuthEndpointEnum;
import main.java.pojo.common.AuthenticateJSON;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class APILoginTest {

    @Test
    public void testLoginFiledIfIncorrectUserData() {
//        AuthenticateJSON authenticateJSON = (AuthenticateJSON) ConnectionManager.ConnectionManagerBuilder.fullUserDataBuild(new ConnectionClass(initUserConnectionData("admin@apriori.com", "admin")))
        AuthenticateJSON authenticateJSON = (AuthenticateJSON) new ConnectionClass().userFullData(this.initUserConnectionData("admin@apriori.com", "admin"))
                .endpoint(AuthEndpointEnum.POST_AUTH)
                .useAutoLogin(false)
                .followRedirection(false)
                .statusCode(200)
                .returnType(AuthenticateJSON.class)
                .connect()
                .post();

        assertNotNull("access token should be present", authenticateJSON.getAccessToken());
//     TODO z: deserialization issue
//        assertNotNull("Expires time should be present", authenticateJSON.getExpires_in());
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
