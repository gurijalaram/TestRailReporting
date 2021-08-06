package help;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;


public class HelpTests extends TestBase {

    private EdcAppLoginPage loginPage;

    @Test
    public void onlineHelpTest() {

        loginPage = new EdcAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .clickHelpDropdown().selectHelp();
    }


}

