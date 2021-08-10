package logout;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

public class LogoutTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private EdcAppLoginPage edcPage;

    @Test
    public void testLogout() {
        String pageTitle = "aPriori Single Sign-On";
        loginPage = new EdcAppLoginPage(driver);
        edcPage = loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .logout();

        assertThat(edcPage.verifyPageTitle(pageTitle), is(true));
    }
}

