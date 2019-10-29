package explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class ActionsTests extends TestBase {
    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    @Category(CustomerSmokeTests.class)
    @Test
    @Issue("BA-843")
    @TestRail(testCaseId = {"545", "731", "738", "1610"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("M3CapScrew.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        explorePage.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("New", "Low", "Qa Description", "\u2022 QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes") //Unicode characters
            .save()
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        scenarioNotesPage = explorePage.selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
    }

    @Test
    @Issue("BA-838")
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
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

        explorePage.openColumnsTable()
            .removeColumn(ColumnsEnum.COST_MATURITY.getColumns())
            .removeColumn(ColumnsEnum.STATUS.getColumns())
            .selectSaveButton();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1610", "592", "593"})
    @Description("User can lock and unlock a scenario")
    public void lockUnlockScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "bracket_basic");

        new GenericHeader(driver).toggleLock();

        new ExplorePage(driver).highlightScenario(testScenarioName, "bracket_basic");

        genericHeader = new GenericHeader(driver);
        genericHeader.selectActions();

        assertThat(genericHeader.isActionLockedStatus("Unlock"), is(true));

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.openScenario(testScenarioName, "bracket_basic");

        assertThat(evaluatePage.isLockedStatus("Locked"), is(true));

        new GenericHeader(driver).toggleLock();

        assertThat(new EvaluatePage(driver).isLockedStatus("Unlocked"), is(true));
    }
}