
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.login.ForgottenPasswordPage;
import com.apriori.utils.login.PrivacyPolicyPage;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class AprioriLoginTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private AprioriLoginPage aprioriLoginPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private ForgottenPasswordPage forgottenPasswordPage;
    //private CloudHomePage cloudHomePage;

    public AprioriLoginTests() {
        super();
    }

    @Before
    public void setup() {
        aprioriLoginPage = new AprioriLoginPage(driver);
    }

    /*@Test
    @TestRail(testCaseId = {"6645"})
    @Description("Test successful login")
    public void testLogin() {

        cloudHomePage = aprioriLoginPage.login(UserUtil.getUser(), CloudHomePage.class);

        assertThat(cloudHomePage.isScenarioCountPresent(), is(true));
    }*/

    @Test
    @TestRail(testCaseId = {"6646"})
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        aprioriLoginPage.failedLoginAs(UserUtil.getUser().getEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(aprioriLoginPage.getLoginErrorMessage()));
    }

    @Test
    @TestRail(testCaseId = {"6647"})
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        aprioriLoginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(aprioriLoginPage.getLoginErrorMessage()));
    }

    @Test
    @TestRail(testCaseId = {"6648"})
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        aprioriLoginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(aprioriLoginPage.getLoginErrorMessage())));
    }

    @Test
    @TestRail(testCaseId = {"6649"})
    @Description("Validate Login Dialog")
    public void loginDialog() {

        assertThat(aprioriLoginPage.isLogoDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6650"})
    @Description("Validate forgotten password link")
    public void forgotPassword() {

        forgottenPasswordPage = aprioriLoginPage.forgottenPassword();

        assertThat(forgottenPasswordPage.getResetPassword(), containsString("Reset your password"));
    }

    @Test
    @TestRail(testCaseId = {"6651"})
    @Description("Validate Welcome Message")
    public void welcomeMessage() {

        assertThat(aprioriLoginPage.getWelcomeText(), containsString("Welcome! This login page provides access to aPriori's web applications, support portal and customer community. Use of aPriori applications is governed by the terms and conditions of your existing SaaS license Agreement with aPriori."));

        privacyPolicyPage = aprioriLoginPage.privacyPolicy();

        assertThat(privacyPolicyPage.getChildWindowURL(), containsString("https://www.apriori.com/privacy-policy"));
        assertThat(privacyPolicyPage.getPageHeading(), containsString("aPriori Technologies, Inc. Privacy Policy"));
    }
}