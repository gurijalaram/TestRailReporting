package main.java.http.builder.service;

import main.java.enums.UsersEnum;
import main.java.http.builder.common.entity.RequestEntity;
import main.java.http.builder.common.entity.UserAuthenticationEntity;
import org.openqa.selenium.WebDriver;

/**
 * HTTP request builder
 */
public class HTTPRequest {

    private WebDriver driver;

    public HTTPRequest(){}

    /**
     * This constructor should be used only for UIvsAPI test cases to avoid double login.
     * For DBvsAPI tests please use constructors which are not using driver as parameter.
     * @param driver - the driver what we are using for test run.
     */
    public HTTPRequest(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Form authorization
     * Using default form parameters, from utils sub-project, for common authorization
     * @param user - Enum with username and password
     * @return
     */
    public RequestInitService defaultFormAuthorization(UsersEnum user) {
        return defaultFormAuthorization(user.getUsername(), user.getPassword());
    }

    /**
     * Form authorization
     * Using default form parameters, from utils sub-project, for common authorization
     * @param username - authorization user email/username
     * @param password - authorization user password
     * @return
     */
    public RequestInitService defaultFormAuthorization(final String username, final String password) {

        return RequestInitService.build(
                RequestEntity.initDefaultFormAuthorizationData(username, password)
        );
    }


    /**
     * Form authorization
     * Use for common authorization with custom form parameters
     * @param userAuthenticationEntity - object with custom form parameters
     * @return
     */
    public RequestInitService customFormAuthorization(UserAuthenticationEntity userAuthenticationEntity) {

        return RequestInitService.build(
                RequestEntity.initCustomFormAuthorizationData(userAuthenticationEntity)
        );
    }

    /**
     * Session authorization
     * Use browser session id for http requests
     * @param emailAddress
     * @param sessionId
     * @param alreadyLoggedIn
     * @return
     */
    public RequestInitService userSessionLigin(String emailAddress, String sessionId, boolean alreadyLoggedIn) {

        return RequestInitService.build(
                RequestEntity.initSessionAuthorizationData(
                        new UserAuthenticationEntity(emailAddress, sessionId, alreadyLoggedIn),
                        driver
                )
        );
    }

    /**
     * URL authorization
     * @param username
     * @param password
     * @return
     */
    //TODO z: as I know, we don't have this authorization type (by URL)
    public RequestInitService userAuthorizationData(final String username, final String password) {

        return RequestInitService.build(
                RequestEntity.initUrlAuthorizationData(username, password)
        );
    }
}
