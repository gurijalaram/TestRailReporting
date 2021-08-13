package help;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.pageobjects.navtoolbars.help.AboutUsPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;


public class HelpTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private AboutUsPage aboutUsPage;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2272"})
    @Description("Be able to access help information in the application header")
    public void onlineHelpTest() {

        loginPage = new EdcAppLoginPage(driver);
        aboutUsPage = loginPage.login(UserUtil.getUser())
            .clickHelpDropdown()
           .selectAbout()
            .switchTab()
            .agreeTermsAndCondition()
            .closeOnlineHelpChat();

//        assertThat(aboutUsPage.getAboutUsPageTitle(), containsString("Product Cost Management Experts"));
    }
}

