package test.java.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

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

    @Test
    @Description("Test successful login")
    public void testLogin() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());
        assertThat(explorePage.isDeleteButtonPresent(), is(true));
    }

    @Test
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs(UsersEnum.CID_TE_USER.getUsername(), "fakePassword");
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs("jacky348@apriori.com", UsersEnum.CID_TE_USER.getPassword());
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs("jacky348@apriori.com", "fakePassword");
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Valid user account must be an email")
    public void testEmailIncorrect() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs("mparker", "fakePassword");
        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }
}