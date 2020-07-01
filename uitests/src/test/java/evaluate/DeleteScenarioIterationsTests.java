package evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
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
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DeleteScenarioIterationsTests extends TestBase {

    private CIDLoginPage loginPage;
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

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFileAndOk(testScenarioName, resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFileAndOk(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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