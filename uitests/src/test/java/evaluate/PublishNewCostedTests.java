package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class PublishNewCostedTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public PublishNewCostedTests() {
        super();
    }

    @Test
    @Description("Edit & publish an existing unlocked scenario from the Public Workspace back to the Public Workspace")
    @Severity(SeverityLevel.NORMAL)
    public void testPublishNewCostedScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Publish Existing Costed Scenario", new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario();

        assertThat(explorePage.findComparison("Publish Existing Costed Scenario").isDisplayed(), is(true));
    }
}