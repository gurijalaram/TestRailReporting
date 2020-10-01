package login;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.pages.login.ForgottenPasswordPage;
import pageobjects.pages.login.PrivacyPolicyPage;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class LoginTests extends TestBase {

    private static String loginPageErrorMessage = "Wrong email or password.";
    File resourceFile;
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private ForgottenPasswordPage forgottenPasswordPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private EvaluatePage evaluatePage;

    public LoginTests() {
        super();
    }

    @Test
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.isDeleteButtonPresent(), is(true));
    }

    @Test
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        loginPage = new CidLoginPage(driver);
        loginPage = loginPage.failedLoginAs(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        loginPage = new CidLoginPage(driver);
        loginPage = loginPage.failedLoginAs("jacky348@apriori.com", UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        loginPage = new CidLoginPage(driver);
        loginPage = loginPage.failedLoginAs("fakeuser@apriori.com", "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1574"})
    @Description("Validate Login Dialog")
    public void loginDialog() {

        loginPage = new CidLoginPage(driver);

        assertThat(loginPage.getMarketingText(), containsString("COST INSIGHT GENERATE:\n" +
                "SOLUTION FOR A NEW NORMAL\n" +
                "Proactively notify your team of manufacturability issues and enable them to optimize their designs faster."));
        assertThat(loginPage.isLogoDisplayed(), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1574"})
    @Description("Validate forgotten password link")
    public void forgotPassword() {

        loginPage = new CidLoginPage(driver);
        forgottenPasswordPage = loginPage.forgottenPassword();

        assertThat(forgottenPasswordPage.getResetPassword(), containsString("Reset your password"));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1575"})
    @Description("Validate Welcome Message")
    public void welcomeMessage() {

        loginPage = new CidLoginPage(driver);
        assertThat(loginPage.getWelcomeText(), containsString("Welcome! This login page provides access to aPriori's web applications, support portal and customer community. Access to these web services is available only to aPriori licensed customers, partners and employees"));

        privacyPolicyPage = loginPage.privacyPolicy();

        assertThat(privacyPolicyPage.getChildWindowURL(), containsString("https://www.apriori.com/privacy-policy"));
        assertThat(privacyPolicyPage.getPageHeading(), containsString("APRIORI TECHNOLOGIES, INC. PRIVACY POLICY"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1590", "1583", "1180"})
    @Description("Validate CAD association remains and attributes can be updated between CID sessions.")
    public void cadConnectionRemains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .openAdminDropdown()
                .selectLogOut();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
                .openScenario(scenarioName, "225_gasket-1-solid1");

        assertThat(evaluatePage.isCADConnectionStatus(), is("CAD file connected"));
    }
}