package main.java;

import main.java.enums.UsersEnum;
import org.openqa.selenium.WebDriver;

/**
 * @author kpatel
 * This class can be initialized with {@link WebDriver} or a {@link UserForAPIConnection} for further communication
 * with internal API endpoints.
 */
public abstract class ConnectionClass {

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

    /**
     * The class is intended to hold necessary user data to be able to login with a user through internal API.
     * You can also pass an already existing JSESSIONID from an already logged in user.
     */
    class UserForAPIConnection {
        private String emailAddress;
        private String password;
        private String sessionId;
        private String grant_type;
        private String client_id;
        private String client_secret;
        private String scope;
        private boolean alreadyLoggedIn;

        UserForAPIConnection(String emailAddress, String password) {
            this.emailAddress = emailAddress;
            this.password = password;
            this.alreadyLoggedIn = false;
        }

        UserForAPIConnection(String emailAddress, String sessionId, boolean alreadyLoggedIn) {
            this.emailAddress = emailAddress;
            this.sessionId = sessionId;
            this.alreadyLoggedIn = alreadyLoggedIn;
        }

        UserForAPIConnection(String emailAddress, String password, String sessionId, String grant_type, String client_id, String client_secret, String scope, boolean alreadyLoggedIn) {
            this.emailAddress = emailAddress;
            this.password = password;
            this.sessionId = sessionId;
            this.grant_type = grant_type;
            this.client_id = client_id;
            this.client_secret = client_secret;
            this.scope = scope;
            this.alreadyLoggedIn = alreadyLoggedIn;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getPassword() {
            return password;
        }

        public boolean getAlreadyLoggedIn() {
            return alreadyLoggedIn;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getGrant_type() {
            return grant_type;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public String getScope() {
            return scope;
        }

        public boolean isAlreadyLoggedIn() {
            return alreadyLoggedIn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            UserForAPIConnection that = (UserForAPIConnection) o;

            if (!emailAddress.equals(that.emailAddress)) {
                return false;
            }
            return password.equals(that.password);
        }

        @Override
        public int hashCode() {
            int result = emailAddress.hashCode();
            result = 31 * result + password.hashCode();
            return result;
        }
    }
}
