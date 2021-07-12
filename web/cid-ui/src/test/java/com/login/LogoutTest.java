package com.login;

import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.pages.login.ForgottenPasswordPage;
import com.pageobjects.pages.login.PrivacyPolicyPage;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LogoutTest extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";
    File resourceFile;
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private ForgottenPasswordPage forgottenPasswordPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private EvaluatePage evaluatePage;

    public LogoutTest() {
        super();
    }

    @Test
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());
        loginPage = explorePage.selectLogOut();

//        assertThat(explorePage.isDeleteButtonPresent(), is(true));
    }
}
