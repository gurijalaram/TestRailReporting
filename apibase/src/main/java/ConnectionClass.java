package main.java;

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

    /**
     * This constructor should be used only for UIvsAPI test cases to avoid double login.
     * For DBvsAPI tests please use constructors which are not using driver as parameter.
     * @param driver - the driver what we are using for test run.
     */
    public ConnectionClass(WebDriver driver) {
        this.driver = driver;
    }

    public ConnectionClass() {
        //this is mostly used for the external API login tests. there is no user needed for the instantiation.
    }


    public ConnectionClass(UserForAPIConnection userForAPIConnection) {
        this.userForAPIConnection = userForAPIConnection;
    }

    public ConnectionClass(UsersEnum user) {
            this.userForAPIConnection = new UserForAPIConnection(user.getUsername(), user.getPassword());
    }

    public ConnectionClass(String emailAddress, String password) {
        this.userForAPIConnection = new UserForAPIConnection(emailAddress, password);
    }

    public ConnectionClass(String emailAddress, String sessionId, boolean alreadyLoggedIn) {
        this.userForAPIConnection = new UserForAPIConnection(emailAddress, sessionId, alreadyLoggedIn);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public UserForAPIConnection getUserForAPIConnection() {
        return userForAPIConnection;
    }


}
