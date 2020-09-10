package login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.homepage.ReportsHomePage;
import pageobjects.pages.login.ReportsLoginPage;
import testsuites.suiteinterface.CIARStagingSmokeTest;

public class LoginTests extends TestBase {

    private ReportsLoginPage loginPage;
    private ReportsHomePage homePage;

    public LoginTests() {
        super();
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = {"2695"})
    @Description("Successful login to CI Report")
    public void testLogin() {
        homePage = new ReportsLoginPage(driver)
            .login();

        assertThat(homePage.isCreateButtonDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2696"})
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        String loginErrorMessage = "Wrong email or password.";
        loginPage = new ReportsLoginPage(driver)
            .failedLogin(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPage.getLoginMessage(), is(equalTo(loginErrorMessage.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"2697"})
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        String passwordResetMsg = "We've just sent you an email to reset your password.";
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.com");

        assertThat(loginPage.getLoginMessage(), is(equalTo(passwordResetMsg.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"2698"})
    @Description("Empty email/password field message displayed")
    public void testEmptyFieldsMessage() {
        String emptyFieldMsg = "Can't be blank";
        loginPage = new ReportsLoginPage(driver)
                .failedLogin("", "");

        assertThat(loginPage.getInputErrorMsg(), is(equalTo(emptyFieldMsg)));
    }

    @Test
    @TestRail(testCaseId = {"2699"})
    @Description("Invalid email address, wrong format")
    public void testInvalidEmail() {
        String invalidEmailMsg = "Invalid";
        loginPage = new ReportsLoginPage(driver)
                .failedLogin("a@b", "fakePassword");

        assertThat(loginPage.getInputErrorMsg(), is(equalTo(invalidEmailMsg)));
    }
}