package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class TwoModelMachiningTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;

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
    @TestRail(testCaseId = {"3927", "3928", "3929", "3930", "3947"})
    public void testTwoModelMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String sourcePartName = "casting_BEFORE_machining";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "casting_AFTER_machining";
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, twoModelPartName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, testScenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, testScenarioName, twoModelFile, currentUser)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
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
    @Description("Validate the User can open the source part in the evaluate tab")
    @TestRail(testCaseId = {"3844", "3941"})
    public void testOpenSourceModel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String sourcePartName = "VulcainCasting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".CATPart");
        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "VulcainMachined";
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        twoModelFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, twoModelPartName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario()
            .openSourceScenario(sourcePartName,sourceScenarioName);

        assertThat(evaluatePage.getCurrentScenarioName(), is(sourceScenarioName));
    }

    @Test
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(testCaseId = {"3940", "3946", "4133"})
    public void multiLevel2Model() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel1ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "2modeltest-cast";
        String twoModel1PartName = "2modeltest-machine1";
        String twoModel2PartName = "2modeltest-machine2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, twoModel1PartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, twoModel2PartName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.getProcessesResult("Utilization"), (closeTo(96.98, 1)));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(15.36, 1));
        assertThat(evaluatePage.getProcessesResult("Finish Mass"), (closeTo(2.33, 1)));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel1PartName, twoModel1ScenarioName, twoModelFile, currentUser)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .submit(EvaluatePage.class)
            .costScenario();

        //assertThat(evaluatePage.getTwoModelUtilizationPercentage(), is(closeTo(82.71, 1)));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(20.33, 1));
        /*assertThat(evaluatePage.getTwoModelFinishMass(), is(closeTo(1.93, 1)));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModel2ScenarioName, twoModelFile2, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(twoModel1ScenarioName, twoModel1PartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getTwoModelUtilizationPercentage(), is(closeTo(83.78, 1)));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(24.54, 1)));
        assertThat(evaluatePage.getTwoModelFinishMass(), is(closeTo(1.62, 1)));*/
    }

    /*@Test
    @Description("Validate the User can open a public source part in the evaluate tab")
    @TestRail(testCaseId = {"4178", "4138"})
    public void testOpenPublicSourceModel() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Raw Casting";
        String twoModelPartName = "Machined Casting";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Raw Casting.prt");
        twoModelFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, "Machined Casting.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", twoModelScenarioName)
            .apply(ExplorePage.class)
            .openScenario(twoModelScenarioName, twoModelPartName)
            .openSourceScenario();

        assertThat(evaluatePage.getCurrentScenarioName(sourceScenarioName), is(true));
    }

    @Test
    @Description("Validate the user can switch the source part")
    @TestRail(testCaseId = {"3845", "4134", "4315"})
    public void switchSourcePart() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String source2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";
        String source2PartName = "Die Casting Lower Control Arm (Source1)";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Cast).SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (Source1).SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, "Die Casting Lower Control Arm (As Machined2).SLDPRT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(source2ScenarioName, twoModelFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile2, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceScenarioName(), is(sourceScenarioName));
        assertThat(evaluatePage.getSourcePartName(), is(sourcePartName.toUpperCase()));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(7.72, 1)));

        evaluatePage.selectSourcePart()
            .highlightScenario(source2ScenarioName, source2PartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceScenarioName(), is(source2ScenarioName));
        assertThat(evaluatePage.getSourcePartName(), is(source2PartName.toUpperCase()));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(8.28, 1)));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate the user cannot use two completely different CAD models")
    @TestRail(testCaseId = {"3948"})
    public void testTwoModelCorrectCADModels() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "casting_before_machining";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        twoModelFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.TWO_MODEL_MACHINING, "casting_AFTER_machining.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), twoModelFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(testScenarioName, "PowderMetalShaft")
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));

        guidancePage = evaluatePage.openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Costing Failed", "Units of the model of the stock differ from the units of the finished model.", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Units of the model of the stock differ from the units of the finished model."));
    }


    @Test
    @Description("Validate the user can fix the source scenario but selecting the continue button to return to the part")
    @TestRail(testCaseId = {"4339"})
    public void continueSourceButton() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Cast).SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Machined2).SLDPRT");

        loginPage = new CidLoginPage(driver);
        sourceCostInvalidPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile2, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario(SourceCostInvalidPage.class);

        assertThat(sourceCostInvalidPage.getCostInvalidText(), containsString("Costing cannot proceed. The source scenario does not have a cost"));

        evaluatePage = sourceCostInvalidPage.selectContinue();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.UNCOSTED_CHANGES.getCostingText()), is(true));
    }

    @Test
    @Description("Validate the user can fix the source scenario but selecting the fix source button, fixing part and recosting 2MM")
    @TestRail(testCaseId = {"4339"})
    public void fixSourceButton() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Cast).SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnum, "Die Casting Lower Control Arm (As Machined2).SLDPRT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile2, EvaluatePage.class)
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
