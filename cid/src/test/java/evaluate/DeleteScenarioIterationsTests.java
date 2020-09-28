package evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DeleteScenarioIterationsTests extends TestBase {

    private CidLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    public DeleteScenarioIterationsTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"588", "394", "581", "395", "589"})
    @Description("Test a public scenario can be deleted from the evaluate page")
    public void testDeletePublicScenarioIteration() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectLock()
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .highlightScenario(testScenarioName, "casting");

        explorePage = new ExplorePage(driver);
        explorePage.editScenario(EvaluatePage.class)
            .delete()
            .deleteScenarioIteration()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "casting"), is(equalTo(0)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"588", "572", "1089"})
    @Description("Test a private scenario can be deleted from the evaluate page")
    public void testDeletePrivateScenarioIteration() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .openScenario(testScenarioName, "casting")
            .delete()
            .deleteScenario()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "casting"), is(equalTo(0)));
    }
}