package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

import java.time.LocalDateTime;

public class PublishExistingCostedTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ComparisonTablePage comparisonTablePage;

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"C389"}, tags = {"smoke"})
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparisonTablePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "testpart-4")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ComparisonTablePage.class);

        assertThat(comparisonTablePage.findScenario(testScenarioName, "testpart-4").isDisplayed(), is(true));
    }
}