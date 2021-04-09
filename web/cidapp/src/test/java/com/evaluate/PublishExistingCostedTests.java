package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishExistingCostedTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "6209")
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "testpart-4";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String filterName = generateStringUtil.generateFilterName();

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".prt");

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .openScenario(testScenarioName, partName)
            .editScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", partName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, partName), is(greaterThan(0)));
    }

    @Test
    @Ignore
    @TestRail(testCaseId = "6210")
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .editScenario()
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .publishScenario()
            .override()
            .continues(PublishPage.class)
            .publish(EvaluatePage.class)
            .lock()
            .publishScenario()
            .publish(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(scenarioNameB, partName), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "6211")
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .uploadComponentAndSubmit(testScenarioName, FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp"), EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .override()
            .continues(PublishPage.class)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Material Stock / Band Saw / Preheat / Hammer / Trim"));
    }

    @Test
    @Ignore
    @TestRail(testCaseId = "6212")
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testScenarioName2 = new GenerateStringUtil().generateScenarioName();
        String partName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .lock()
            .uploadComponentAndSubmit(testScenarioName, FileResourceUtil.getCloudFile(processGroupEnum, partName + ".stp"), EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .changeName(testScenarioName2)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(equalTo(testScenarioName2)));
    }
}