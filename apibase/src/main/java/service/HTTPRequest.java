package main.java.service;

import main.java.common.UserForAPIConnection;
import main.java.dao.ConnectionManager;
import main.java.enums.UsersEnum;
import org.openqa.selenium.WebDriver;

public class HTTPRequest {

//    private WebDriver driver;
//    private UserForAPIConnection userForAPIConnection;
//
//
//    public HTTPRequest(){}
//
//    /**
//     * This constructor should be used only for UIvsAPI test cases to avoid double login.
//     * For DBvsAPI tests please use constructors which are not using driver as parameter.
//     * @param driver - the driver what we are using for test run.
//     */
//    public HTTPRequest(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public ConnectionManager.ConnectionManagerBuilder userAuthorizationData(final String username, final String password) {
//        this.userForAPIConnection = new UserForAPIConnection(username, password);
//        return new ConnectionManager.ConnectionManagerBuilder(this);
//    }
//
//    public ConnectionManager.ConnectionManagerBuilder userFullData(UserForAPIConnection userForAPIConnection) {
//        this.userForAPIConnection = userForAPIConnection;
//        return new ConnectionManager.ConnectionManagerBuilder(this, true);
//    }


}
