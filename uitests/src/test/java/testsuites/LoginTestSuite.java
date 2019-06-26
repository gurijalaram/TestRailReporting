package test.java.testsuites;

import main.java.ConnectionClass;
import main.java.ConnectionManager;
import main.java.UserForAPIConnection;
import main.java.enums.common.AuthEndpointEnum;
import main.java.pojo.common.AuthenticateJSON;
import org.junit.Test;

//TODO z: just in case of clear run
//@RunWith(JUnit4.class)
//@Suite.SuiteClasses({
//        LoginTests.class,
//})
public class LoginTestSuite {

    @Test
    public void testLoginFiledIfIncorrectUserData() {

    ConnectionManager.ConnectionManagerBuilder.fullUserDataBuild(new ConnectionClass(initUserConnectionData("cfrith@apriori.com","TestEvent2018")))
              .endpoint(AuthEndpointEnum.POST_AUTH)
              .useAutoLogin(false)
              .followRedirection(false)
              .statusCode(200)
              .returnType(AuthenticateJSON.class)
              .connect()
              .post();

    }

    public UserForAPIConnection initUserConnectionData (final String username, final String password) {
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
