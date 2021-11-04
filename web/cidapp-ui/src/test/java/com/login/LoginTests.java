package com.login;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.css.entity.response.Item;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.login.ForgottenPasswordPage;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class LoginTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ForgottenPasswordPage forgottenPasswordPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private UserCredentials currentUser;
    private Item cssItem;

    public LoginTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6645"})
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.isScenarioCountPresent(), is(true));
    }

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6646"})
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }*/

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6647"})
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }

    @Test
    @TestRail(testCaseId = {"6648"})
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        loginPage = new CidAppLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @TestRail(testCaseId = {"6649"})
    @Description("Validate Login Dialog")
    public void loginDialog() {

        loginPage = new CidAppLoginPage(driver);

        assertThat(loginPage.getMarketingText(), containsString("aPriori Cost Insight\n" +
            "Explore the Possible"));
        assertThat(loginPage.isLogoDisplayed(), is(true));
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"6650"})
    @Description("Validate forgotten password link")
    public void forgotPassword() {

        loginPage = new CidAppLoginPage(driver);
        forgottenPasswordPage = loginPage.forgottenPassword();

        assertThat(forgottenPasswordPage.getResetPassword(), containsString("Reset your password"));
    }

    @Test
    @TestRail(testCaseId = {"6651"})
    @Description("Validate Welcome Message")
    public void welcomeMessage() {

        loginPage = new CidAppLoginPage(driver);
        assertThat(loginPage.getWelcomeText(), containsString("Welcome! This login page provides access to aPriori's web applications, support portal and customer community. Use of aPriori applications is governed by the terms and conditions of your existing SaaS license Agreement with aPriori."));

        privacyPolicyPage = loginPage.privacyPolicy();

        assertThat(privacyPolicyPage.getChildWindowURL(), containsString("https://www.apriori.com/privacy-policy"));
        assertThat(privacyPolicyPage.getPageHeading(), containsString("aPriori Technologies, Inc. Privacy Policy"));
    }

    @Test
    @TestRail(testCaseId = {"6652"})
    @Description("Validate CAD association remains and attributes can be updated between CID sessions.")
    public void cadConnectionRemains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "225_gasket-1-solid1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, EvaluatePage.class)
            .logout()
            .login(UserUtil.getUser())
            .selectFilter("Public")
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD), is(true));
    }*/
}