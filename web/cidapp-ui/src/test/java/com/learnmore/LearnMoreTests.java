package com.learnmore;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.login.ForgottenPasswordPage;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LearnMoreTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ForgottenPasswordPage forgottenPasswordPage;
    private PrivacyPolicyPage privacyPolicyPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private UserCredentials currentUser;

    public LearnMoreTests() {
        super();
    }

    @Test
    @Description("Test to access learn more feature")
    public void testLearnMoreFeatureOnNewPage() {
        //Given
        //Expectation Message
        String webinarTitle = "Webinar | Cost Insight Generate: Solution for a New Normal";

        //When
        loginPage = new CidAppLoginPage(driver);
        String title = loginPage
                .openLearnMore()
                .getWebinarTitle();

        //Then
        assertThat(title, is(webinarTitle));
    }

}
