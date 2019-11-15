package login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import reports.pages.homepage.HomePage;
import reports.pages.login.LoginPage;

import java.io.IOException;

public class ReportLoginTests extends TestBase {

    private LoginPage loginPage;
    private HomePage homePage;
    private static String loginErrorMessage = "Wrong email or password.";
    private static String passwordResetMsg = "We've just sent you an email to reset your password.";
    private static String emptyFieldMsg = "Can't be blank";
    private static String invalidEmailMsg = "Invalid";

    public ReportLoginTests() {
        super();
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    @TestRail(testCaseId = {"2695"})
    @Description("Successful login to CI Report")
    public void testLogin() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UserUtil.getUser());
        assertThat(homePage.isCreateButtonDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2696"})
    @Description("Failed login to CI Report, wrong password")
    public void failedLogin() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin(UserUtil.getUser().getUsername(), "fakePassword");
        assertThat(loginPage.getLoginMessage(), is(equalTo(loginErrorMessage.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"2697"})
    @Description("Forgotten password functionality")
    public void forgotPassword() {
        loginPage = new LoginPage(driver);
        loginPage.clickForgotPassword()
            .submitEmail("fakeEmail@apriori.com");
        assertThat(loginPage.getLoginMessage(), is(equalTo(passwordResetMsg.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"2698"})
    @Description("Empty email/password field message displayed")
    public void emptyFieldsMessage() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin("", "");
        assertThat(loginPage.getInputErrorMsg(), is(equalTo(emptyFieldMsg)));
    }

    @Test
    @TestRail(testCaseId = {"2699"})
    @Description("Invalid email address, wrong format")
    public void invalidEmail() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin("a@b", "fakePassword");
        assertThat(loginPage.getInputErrorMsg(), is(equalTo(invalidEmailMsg)));
    }

    @Test
    @TestRail(testCaseId = {"2700"})
    @Description("Link to privacy policy working")
    public void testPrivacyPolicyLink() throws IOException {
        loginPage = new LoginPage(driver);
        loginPage.init(driver, Constants.cirURL, true);
        loginPage.isPrivacyPolicyButtonOnScreen();
        assertThat(loginPage.isPrivacyPolicyButtonDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2701"})
    @Description("Link to help page working")
    public void testHelpLink() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UserUtil.getUser())
                .waitForHelpButtonVisibility();
        assertThat(homePage.isHelpButtonDisplayed(), is(true));
    }
}
