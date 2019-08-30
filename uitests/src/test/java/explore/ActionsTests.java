package test.java.explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.ColumnsEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.explore.ScenarioNotesPage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

public class ActionsTests extends TestBase {
    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;

    @Test
    @TestRail(testCaseId = {"C545"}, tags = {"smoke"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {

        String testScenarioName = Constants.scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("M3CapScrew.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        explorePage.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("New", "Low", "Qa Description", "\u2022 QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes")
            .save()
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        scenarioNotesPage = explorePage.selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
    }

    @Test
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {

        String testScenarioName = Constants.scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("M3CapScrew.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        explorePage.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Analysis", "Medium", "Qa Description", "Adding QA notes")
            .save()
            .openColumnsTable()
            .addColumn(ColumnsEnum.COST_MATURITY.getColumns())
            .addColumn(ColumnsEnum.STATUS.getColumns())
            .selectSaveButton();

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.STATUS.getColumns(), ColumnsEnum.COST_MATURITY.getColumns()));
    }
}