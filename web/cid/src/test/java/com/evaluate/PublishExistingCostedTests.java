package com.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.PublishWarningPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.jobqueue.JobQueuePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.GenericHeader;
import com.testsuites.suiteinterface.SmokeTests;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

public class PublishExistingCostedTests extends TestBase {

    private CidLoginPage loginPage;
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

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "testpart-4";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".prt");

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ExplorePage.class);

        assertThat(explorePage.findScenario(testScenarioName, partName).isDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"390", "569", "403"})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
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
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @Issue("TE-5553")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"391"})
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .refreshCurrentPage()
            .uploadFileAndOk(testScenarioName, FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp"), EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishWarningPage.class)
            .selectOverwriteOption()
            .selectContinueButton()
            .selectPublishButton()
            .openScenario(testScenarioName, partName);

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Material Stock / Band Saw / Preheat / Hammer / Trim"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"393"})
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testScenarioName2 = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectLock()
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .refreshCurrentPage()
            .uploadFileAndOk(testScenarioName, FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp"), EvaluatePage.class)
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