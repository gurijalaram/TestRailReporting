package login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reports.pages.homepage.HomePage;
import reports.pages.login.LoginPage;

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
    @Description("Successful login to CI Report")
    public void testLogin() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());
        assertThat(homePage.isCreateButtonDisplayed(), is(true));
    }

    @Test
    @Description("Failed login to CI Report")
    public void failedLogin() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin(UsersEnum.CID_TE_USER.getUsername(), "fakePassword");
        assertThat(loginPage.getLoginMessage(), is(equalTo(loginErrorMessage.toUpperCase())));
    }

    @Test
    @Description("Forgotten password functionality")
    public void forgotPassword() {
        loginPage = new LoginPage(driver);
        loginPage.clickForgotPassword()
                .submitEmail("fakeEmail@apriori.com");
        assertThat(loginPage.getLoginMessage(), is(equalTo(passwordResetMsg.toUpperCase())));
    }

    @Test
    @Description("Empty email/password field message displayed")
    public void emptyFieldsMessage() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin("", "");
        assertThat(loginPage.getInputErrorMsg(), is(equalTo(emptyFieldMsg)));
    }

    @Test
    @Description("Invalid email address, wrong format")
    public void invalidEmail() {
        loginPage = new LoginPage(driver);
        loginPage.failedLogin("a@b", "fakePassword");
        assertThat(loginPage.getInputErrorMsg(), is(equalTo(invalidEmailMsg)));
    }
}
