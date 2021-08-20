package com.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.ElectronicsDataCollectionPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.EDCSmokeTestSuite;

public class LoginTests extends TestBase {

    public LoginTests() {
        super();
    }

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private EdcAppLoginPage loginPage;
    private ElectronicsDataCollectionPage edcPage;

    @Test
    @Category(EDCSmokeTestSuite.class)
    @TestRail(testCaseId = {"8886"})
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new EdcAppLoginPage(driver);
        edcPage = loginPage.login(UserUtil.getUser());

        assertThat(edcPage.getUploadedBillOfMaterials(), is(equalTo("Uploaded Bill of Materials")));
    }

    @Test
    @Category(EDCSmokeTestSuite.class)
    @TestRail(testCaseId = {"8889"})
    @Description("Test unsuccessful login with incorrect email and incorrect password")
    public void testIncorrectEmailAndPassword() {
        loginPage = new EdcAppLoginPage(driver);
        loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }

    @Test
    @Category(EDCSmokeTestSuite.class)
    @TestRail(testCaseId = {"8890"})
    @Description("Test unsuccessful login with correct email and incorrect password")
    public void testEmailAndIncorrectPassword() {
        loginPage = new EdcAppLoginPage(driver);
        loginPage.failedLoginAs(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }
}
