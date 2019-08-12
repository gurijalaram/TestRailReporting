package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class PublishExistingCostedTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparisonTablePage comparisonTablePage;

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    @Severity(SeverityLevel.NORMAL)
    public void testPublishExistingCostedScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        comparisonTablePage = explorePage.uploadFile("Publish Existing Costed Scenario", new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario("Publish Existing Costed Scenario", "testpart-4")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ComparisonTablePage.class);

        assertThat(comparisonTablePage.findComparison("Publish Existing Costed Scenario", "testpart-4").isDisplayed(), is(true));
    }
}