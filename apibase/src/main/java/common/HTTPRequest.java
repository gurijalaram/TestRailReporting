package main.java.common;

import main.java.enums.UsersEnum;
import main.java.service.RequestDataInit;
import org.openqa.selenium.WebDriver;

/**
 * @author kpatel
 * This class can be initialized with {@link WebDriver} or a {@link UserForAPIConnection} for further communication
 * with internal API endpoints.
 */
public class HTTPRequest {

    private WebDriver driver;
    private UserForAPIConnection userForAPIConnection;


    public HTTPRequest(){}

    /**
     * This constructor should be used only for UIvsAPI test cases to avoid double login.
     * For DBvsAPI tests please use constructors which are not using driver as parameter.
     * @param driver - the driver what we are using for test run.
     */
    public HTTPRequest(WebDriver driver) {
        this.driver = driver;
    }

    public RequestDataInit userEnum(UsersEnum user) {
        return defaultFormAuthorization(user.getUsername(), user.getPassword());
    }

    public RequestDataInit userAuthorizationData(final String username, final String password) {
        this.userForAPIConnection = new UserForAPIConnection(username, password);
        return RequestDataInit.buildWithUrlAuthorization(this);
    }

    public RequestDataInit defaultFormAuthorization(final String username, final String password) {
        this.userForAPIConnection = new UserForAPIConnection(username, password);

        return RequestDataInit.buildWithDefaultAuthorization(this);
    }

    public RequestDataInit customFormAuthorization(UserForAPIConnection userForAPIConnection) {
        this.userForAPIConnection = userForAPIConnection;
        return RequestDataInit.buildWithCustomAuthorization(this);
    }

    public RequestDataInit userSessionLigin(String emailAddress, String sessionId, boolean alreadyLoggedIn) {
        this.userForAPIConnection = new UserForAPIConnection(emailAddress, sessionId, alreadyLoggedIn);
        return RequestDataInit.buildWithUrlAuthorization(this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public UserForAPIConnection getUserForAPIConnection() {
        return userForAPIConnection;
    }


}
