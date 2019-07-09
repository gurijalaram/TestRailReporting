package main.java.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private static String loginPageErrorMessage = "Wrong email or password.";

    public LoginTests() {
        super();
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Test successful login
     */
    @Test
    public void testLogin() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());
        assertThat(explorePage.isDeleteButtonPresent(), is(true));
    }

    /**
     * Test unsuccessful login with correct email, incorrect password
     */
    @Test
    public void testIncorrectPwd() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs(UsersEnum.CID_TE_USER.getUsername(), "asdfdasfas");
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    /**
     * Test unsuccessful login with incorrect email, correct password
     */
    @Test
    public void testIncorrectEmail() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs("jacky348", UsersEnum.CID_TE_USER.getPassword());
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }
}  
