package help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.help.OnlineHelpPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class HelpTests extends TestBase {

    private LoginPage loginPage;
    private OnlineHelpPage onlineHelpPage;

    @Test
    @TestRail(testCaseId = {""})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void onlineHelpTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .openHelpMenu()
            .clickOnlineHelp();

        assertThat(onlineHelpPage.getPageTitle(), containsString("Cost Insight Design:User Guide"));
    }
}
