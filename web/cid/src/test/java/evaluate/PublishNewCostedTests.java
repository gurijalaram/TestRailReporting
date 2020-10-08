package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishNewCostedTests extends TestBase {

    private CidLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    public PublishNewCostedTests() {
        super();
    }

    @Test
    @Category({SmokeTests.class, SanityTests.class})
    @Description("Publish a new scenario from the Private Workspace to the Public Workspace")
    @TestRail(testCaseId = {"386", "388"})
    public void testPublishNewCostedScenario() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "testpart-4";
        resourceFile = FileResourceUtil.getResourceAsFile(partName + ".prt");

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        assertThat(explorePage.getListOfScenarios(testScenarioName, partName) > 0, is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"400", "401", "402", "404", "525", "1610"})
    @Description("Publish a part and add an assignee, cost maturity and status")
    public void testPublishWithStatus() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "testpart-4";
        resourceFile = FileResourceUtil.getResourceAsFile(partName + ".prt");

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario("Analysis", "Low", "Abe Chaves")
            .selectPublishButton();

        assertThat(explorePage.getListOfScenarios(testScenarioName, partName) > 0, is(true));
    }
}