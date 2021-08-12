package help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.pageobjects.navtoolbars.AboutUsPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;


public class HelpTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private AboutUsPage aboutUsPage;

    @Test
    public void onlineHelpTest() {

        loginPage = new EdcAppLoginPage(driver);
        aboutUsPage = loginPage.login(UserUtil.getUser())
            .clickHelpDropdown()
           .selectAbout()
            .switchTab()
            .agreeTermsAndCondition();
//            .closeOnlineHelpChat();

        assertThat(aboutUsPage.getAboutUsPageTitle(), containsString("Product Cost Management Experts"));
    }
}

