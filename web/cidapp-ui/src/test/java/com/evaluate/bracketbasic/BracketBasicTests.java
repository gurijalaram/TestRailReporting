package com.evaluate.bracketbasic;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.SourceModelExplorePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.login.ForgottenPasswordPage;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

@Slf4j
public class BracketBasicTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ForgottenPasswordPage forgottenPasswordPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private EvaluatePage evaluatePage;
    private SourceModelExplorePage sourceModelExplorePage;


    private File resourceFile;
    private UserCredentials currentUser;

    public BracketBasicTests() {
        super();
    }

    @Test
    @Description("Test successful login")
    public void testLogin() {

        currentUser = UserUtil.getUser();
        String componentName = "BRACKET_BASIC";
        String scenarioName = "Gatling_ddd317c0-3868-4542-b0ed-3041f7ddd054";
        loginPage = new CidAppLoginPage(driver);

        loginPage
                .login(currentUser)
                .clickSearch("bracket_basic")
                .setPagination()
                .openScenario(componentName, scenarioName)
                .openMaterialProcess()
                .goToPartNestingTab();
    }
}
