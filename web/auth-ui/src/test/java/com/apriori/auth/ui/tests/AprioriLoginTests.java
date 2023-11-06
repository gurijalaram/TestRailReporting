package com.apriori.auth.ui.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.login.ForgottenPasswordPage;
import com.apriori.web.app.util.login.LoginService;
import com.apriori.web.app.util.login.PrivacyPolicyPage;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AprioriLoginTests extends TestBaseUI {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private LoginService aprioriLoginService;
    private PrivacyPolicyPage privacyPolicyPage;
    private ForgottenPasswordPage forgottenPasswordPage;
    //private CloudHomePage cloudHomePage;

    public AprioriLoginTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        aprioriLoginService = new LoginService(driver, "");
    }

    /*@Test
    @TestRail(id = {6645})
    @Description("Test successful login")
    public void testLogin() {

        cloudHomePage = aprioriLoginService.login(UserUtil.getUser(), CloudHomePage.class);

        assertThat(cloudHomePage.isScenarioCountPresent(), is(true));
    }*/

    @Test
    @TestRail(id = {6646})
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {
        aprioriLoginService.failedLoginEmptyFields();
        aprioriLoginService.loginNoReturn(UserUtil.getUser().getEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(aprioriLoginService.getLoginErrorMessage()));
    }

    @Test
    @TestRail(id = {6647})
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        aprioriLoginService.loginNoReturn(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(aprioriLoginService.getLoginErrorMessage()));
    }

    @Test
    @TestRail(id = {6648})
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        aprioriLoginService.loginNoReturn(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(aprioriLoginService.getLoginErrorMessage())));
    }

    @Test
    @TestRail(id = {6649})
    @Description("Validate Login Dialog")
    public void loginDialog() {

        assertThat(aprioriLoginService.isLogoDisplayed(), is(true));
    }

    @Test
    @TestRail(id = {6650})
    @Description("Validate forgotten password link")
    public void forgotPassword() {

        forgottenPasswordPage = aprioriLoginService.forgottenPassword(ForgottenPasswordPage.class);

        assertThat(forgottenPasswordPage.getResetPassword(), containsString("Reset your password"));
    }

    @Test
    @TestRail(id = {6651})
    @Description("Validate Welcome Message")
    public void welcomeMessage() {

        assertThat(aprioriLoginService.getWelcomeText(), containsString(
            "Welcome! This login page provides access to aPriori's web applications, " +
                "support portal and customer community. Use of aPriori applications is governed by the " +
                "terms and conditions of your existing SaaS license Agreement with aPriori."));

        privacyPolicyPage = aprioriLoginService.privacyPolicy(PrivacyPolicyPage.class);

        assertThat(privacyPolicyPage.getChildWindowURL(), containsString(
            "https://www.apriori.com/privacy-policy"));
        assertThat(privacyPolicyPage.getPageHeading(), containsString(
            "aPriori Technologies, Inc. Privacy Policy"));
    }
}