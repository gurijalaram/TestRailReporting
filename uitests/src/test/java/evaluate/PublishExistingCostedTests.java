package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.PublishWarningPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishExistingCostedTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    private File resourceFile;

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"389", "1091"})
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {

        String testScenarioName = new Util().getScenarioName();
        String partName = "testpart-4";
        resourceFile = new FileResourceUtil().getResourceFile(partName + ".prt");

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, partName)
            .editScenario(EvaluatePage.class)
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", partName)
            .apply(ExplorePage.class);

        assertThat(explorePage.findScenario(testScenarioName, partName).isDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"390", "569", "403"})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {

        String testScenarioName = new Util().getScenarioName();
        String scenarioNameB = new Util().getScenarioName();
        String partName = "PowderMetalShaft";
        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, partName);

        genericHeader = new GenericHeader(driver);
        genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueActionStatus(partName, testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        explorePage = explorePage.openScenario(testScenarioName, partName)
            .editScenario(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .publishScenario(PublishWarningPage.class)
            .enterNewScenarioName(scenarioNameB)
            .selectContinueButton()
            .selectLock()
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(partName, scenarioNameB, "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace());

        assertThat(explorePage.findScenario(scenarioNameB, partName).isDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"391"})
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {

        String testScenarioName = new Util().getScenarioName();
        String partName = "PowderMetalShaft";
        resourceFile = new FileResourceUtil().getResourceFile(partName + ".stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .refreshCurrentPage()
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile(partName + ".stp"))
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishWarningPage.class)
            .selectOverwriteOption()
            .selectContinueButton()
            .selectPublishButton()
            .openScenario(testScenarioName, partName);

        assertThat(evaluatePage.isProcessRoutingDetails("Material Stock / Band Saw / Preheat / Hammer / Trim"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"393"})
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {

        String testScenarioName = new Util().getScenarioName();
        String testScenarioName2 = new Util().getScenarioName();
        String partName = "PowderMetalShaft";
        resourceFile = new FileResourceUtil().getResourceFile(partName + ".stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectLock()
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .refreshCurrentPage()
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile(partName + ".stp"))
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishWarningPage.class)
            .enterNewScenarioName(testScenarioName2)
            .selectContinueButton()
            .selectPublishButton()
            .openScenario(testScenarioName2, partName);

        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName2), is(true));
    }
}