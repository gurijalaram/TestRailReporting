package main.java.common;

import main.java.common.UserForAPIConnection;
import main.java.dao.ConnectionManager;
import main.java.enums.UsersEnum;
import org.openqa.selenium.WebDriver;

/**
 * @author kpatel
 * This class can be initialized with {@link WebDriver} or a {@link UserForAPIConnection} for further communication
 * with internal API endpoints.
 */
public class ConnectionClass {

    private WebDriver driver;
    private UserForAPIConnection userForAPIConnection;


    public ConnectionClass(){}

    /**
     * This constructor should be used only for UIvsAPI test cases to avoid double login.
     * For DBvsAPI tests please use constructors which are not using driver as parameter.
     * @param driver - the driver what we are using for test run.
     */
    public ConnectionClass(WebDriver driver) {
        this.driver = driver;
    }

    public ConnectionManager.ConnectionManagerBuilder userEnum(UsersEnum user) {
        return userAuthorizationData(user.getUsername(), user.getPassword());
    }

    public ConnectionManager.ConnectionManagerBuilder userAuthorizationData(final String username, final String password) {
        this.userForAPIConnection = new UserForAPIConnection(username, password);
        return new ConnectionManager.ConnectionManagerBuilder(this);
    }

    public ConnectionManager.ConnectionManagerBuilder userFullData(UserForAPIConnection userForAPIConnection) {
        this.userForAPIConnection = userForAPIConnection;
        return new ConnectionManager.ConnectionManagerBuilder(this, true);
    }

    public ConnectionManager.ConnectionManagerBuilder userSessionLigin(String emailAddress, String sessionId, boolean alreadyLoggedIn) {
        this.userForAPIConnection = new UserForAPIConnection(emailAddress, sessionId, alreadyLoggedIn);
        return new ConnectionManager.ConnectionManagerBuilder(this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public UserForAPIConnection getUserForAPIConnection() {
        return userForAPIConnection;
    }


}
