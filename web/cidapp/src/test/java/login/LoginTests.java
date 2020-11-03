package login;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidAppLoginPage;
import testsuites.suiteinterface.SmokeTests;

public class LoginTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    public LoginTests() {
        super();
    }

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    @Test
    @Category(SmokeTests.class)
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.isFilterButtonPresent(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate Login Dialog")
    public void loginDialog() {

        loginPage = new CidAppLoginPage(driver);

        assertThat(loginPage.getMarketingText(), containsString("COST INSIGHT GENERATE:\n" +
            "SOLUTION FOR A NEW NORMAL\n" +
            "Proactively notify your team of manufacturability issues and enable them to optimize their designs faster."));
        assertThat(loginPage.isLogoDisplayed(), is(true));
    }
}