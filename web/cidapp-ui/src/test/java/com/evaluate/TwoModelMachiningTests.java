package com.evaluate;

import static com.apriori.utils.enums.ProcessGroupEnum.CASTING_DIE;
import static com.apriori.utils.enums.ProcessGroupEnum.TWO_MODEL_MACHINING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.EvaluateDfmIconEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class TwoModelMachiningTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private GuidanceIssuesPage guidanceIssuesPage;

    private File resourceFile;
    private File twoModelFile;
    private File twoModelFile2;
    private UserCredentials currentUser;

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate Source and util tile appears when 2 MM is selected")
    @TestRail(testCaseId = {"7861", "7862", "7863", "7864", "7870"})
    public void testTwoModelMachining() {
        final ProcessGroupEnum processGroupEnum = CASTING_DIE;

        String sourcePartName = "casting_BEFORE_machining";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "casting_AFTER_machining";
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, twoModelPartName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, testScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .selectFilter("Recent")
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, testScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceModelMaterial(), is("Aluminum, Cast, ANSI AL380.0"));
        assertThat(evaluatePage.getSourcePartDetails(), containsString(sourcePartName.toUpperCase()));
        assertThat(evaluatePage.getSourcePartDetails(), containsString(testScenarioName));

        /*processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Source Component")
            .selectOptions()
            .selectOverrideSensitivityButton()
            .setCadModelSensitivity("42")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Source Component")
            .selectOptions();

        assertThat(processSetupOptionsPage.getCadModelSensitivity(), is("42"));*/
    }

    @Test
    @Issue("BA-1921")
    @Description("Validate the User can open the source part in the evaluate tab")
    @TestRail(testCaseId = {"6466", "7866"})
    public void testOpenSourceModel() {
        final ProcessGroupEnum processGroupEnum = CASTING_DIE;

        String sourcePartName = "VulcainCasting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".CATPart");
        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "VulcainMachined";
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        twoModelFile = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModelPartName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.MEDIUM.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario()
            .openSourceScenario(sourcePartName, sourceScenarioName);

        assertThat(evaluatePage.getCurrentScenarioName(), is(sourceScenarioName));
    }

    @Test
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(testCaseId = {"7865", "7869", "7872"})
    public void multiLevel2Model() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel1ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "2modeltest-cast";
        String twoModel1PartName = "2modeltest-machine1";
        String twoModel2PartName = "2modeltest-machine2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModel1PartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModel2PartName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessesResult("Utilization"), (closeTo(96.98, 1)));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(18.88, 5));
        assertThat(evaluatePage.getProcessesResult("Finish Mass"), (closeTo(2.33, 1)));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel1PartName, twoModel1ScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessesResult("Utilization"), (closeTo(82.70, 1)));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(25.81, 1));
        assertThat(evaluatePage.getProcessesResult("Finish Mass"), (closeTo(1928, 1)));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel1PartName, twoModel2ScenarioName, twoModelFile2, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(twoModel1PartName, twoModel1ScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessesResult("Utilization"), (closeTo(83.78, 1)));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(30.67, 1));
        assertThat(evaluatePage.getProcessesResult("Finish Mass"), (closeTo(1615, 1)));
    }

    @Test
    @Issue("BA-1885")
    @Description("Validate the User can open a public source part in the evaluate tab")
    @TestRail(testCaseId = {"7867", "7876"})
    public void testOpenPublicSourceModel() {
        final ProcessGroupEnum processGroupEnum = CASTING_DIE;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Raw Casting";
        String twoModelPartName = "Machined Casting";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".prt");
        twoModelFile = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModelPartName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(CASTING_DIE)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .openSourceScenario(sourcePartName, sourceScenarioName);

        assertThat(evaluatePage.getCurrentScenarioName(), is(sourceScenarioName));
    }

    @Test
    @Issue("MIC-3139")
    @Description("Validate the user can switch the source part")
    @TestRail(testCaseId = {"6467", "7873", "7874"})
    public void switchSourcePart() {
        final ProcessGroupEnum processGroupEnum = CASTING_DIE;
        currentUser = UserUtil.getUser();

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String source2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";
        String source2PartName = "Die Casting Lower Control Arm (Source1)";
        String twoModelPartName = "Die Casting Lower Control Arm (As Machined2)";


        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, source2PartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModelPartName + ".SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(CASTING_DIE)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(source2PartName, source2ScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(CASTING_DIE)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile2, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourcePartDetails(), containsString(sourcePartName.toUpperCase()));
        assertThat(evaluatePage.getSourcePartDetails(), containsString(sourceScenarioName));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(10.53, 1));

        evaluatePage.selectSourcePart()
            .highlightScenario(source2PartName, source2ScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourcePartDetails(), containsString(source2PartName.toUpperCase()));
        assertThat(evaluatePage.getSourcePartDetails(), containsString(source2ScenarioName));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(11.66, 1));
    }

    @Test
    @Description("Validate the user cannot use two completely different CAD models")
    @TestRail(testCaseId = {"7871"})
    public void testTwoModelCorrectCADModels() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;
        currentUser = UserUtil.getUser();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "casting_before_machining";
        String wrongSourcePartName = "PowderMetalShaft";
        String twoModelPartName = "casting_AFTER_machining";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, wrongSourcePartName + ".stp");
        twoModelFile = FileResourceUtil.getCloudFile(TWO_MODEL_MACHINING, twoModelPartName + ".stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(wrongSourcePartName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, testScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(TWO_MODEL_MACHINING)
            .selectSourcePart()
            .highlightScenario(wrongSourcePartName, testScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UNCOSTED_CHANGES), is(true));

        /*guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Costing Failed", "Units of the model of the stock differ from the units of the finished model.", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Units of the model of the stock differ from the units of the finished model."));*/
    }


    /*@Test
    @Description("Validate the user can fix the source scenario but selecting the continue button to return to the part")
    @TestRail(testCaseId = {"7879"})
    public void continueSourceButton() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";
        String twoModelPartName = "Die Casting Lower Control Arm (As Machined2)";



        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnum, twoModelPartName + ".SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        sourceModelInvalidPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile2, currentUser)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .submit(EvaluatePage.class)
            .costScenario(SourceModelInvalidPage.class);

        assertThat(sourceModelInvalidPage.getCostInvalidText(), containsString("Costing cannot proceed. The source scenario does not have a cost"));

        evaluatePage = sourceModelInvalidPage.clickIgnore();
    }*/

    /*@Test
    @Description("Validate the user can fix the source scenario but selecting the fix source button, fixing part and recosting 2MM")
    @TestRail(testCaseId = {"7879"})
    public void fixSourceButton() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Cast).SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Machined2).SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOpen(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadComponentAndOpen(twoModelScenarioName, twoModelFile2, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario(SourceCostInvalidPage.class)
            .selectFixSource();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), is(true));
        assertThat(evaluatePage.getPartName(), is("Die Casting Lower Control Arm (As Cast)".toUpperCase()));

        evaluatePage.inputProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(twoModelScenarioName, "Die Casting Lower Control Arm (As Machined2)")
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }*/
}
