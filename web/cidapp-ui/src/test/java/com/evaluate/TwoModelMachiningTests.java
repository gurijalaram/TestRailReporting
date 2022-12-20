package com.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.EvaluateDfmIconEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
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
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private GuidanceIssuesPage guidanceIssuesPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate Source and util tile appears when 2 MM is selected")
    @TestRail(testCaseId = {"7861", "7862", "7863", "7864", "7870"})
    public void testTwoModelMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String sourcePartName = "casting_BEFORE_machining";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "casting_AFTER_machining";
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, twoModelPartName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, testScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getSourceModelMaterial()).isEqualTo("Aluminum, ANSI AL380.0");
        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(testScenarioName)).isEqualTo(true);

        softAssertions.assertAll();

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
    @TestRail(testCaseId = {"6466", "7866", "12511"})
    public void testOpenSourceModel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourcePartName = "VulcainCasting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".CATPart");
        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelPartName = "VulcainMachined";
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModelPartName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario(5)
            .openSourceScenario(sourceScenarioName);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(sourceScenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(testCaseId = {"7865", "7869", "7872"})
    public void multiLevel2Model() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel1ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "2modeltest-cast";
        String twoModel1PartName = "2modeltest-machine1";
        String twoModel2PartName = "2modeltest-machine2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModel1PartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModel2PartName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(96.34), Offset.offset(5.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(18.88), Offset.offset(5.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(2.33), Offset.offset(5.0));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel1PartName, twoModel1ScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(82.70), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(28.2), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(1.93), Offset.offset(10.0));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel2PartName, twoModel2ScenarioName, twoModelFile2, currentUser)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(twoModel1PartName)
            .highlightScenario(twoModel1PartName, twoModel1ScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(83.78), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(30.49), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(1.62), Offset.offset(10.0));

        softAssertions.assertAll();
    }

    @Test
    @Description("Validate the User can open a public source part in the evaluate tab")
    @TestRail(testCaseId = {"7867", "7876"})
    public void testOpenPublicSourceModel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Raw Casting";
        String twoModelPartName = "Machined Casting";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".prt");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModelPartName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(sourcePartName, sourceScenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .uploadComponent(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser);

        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .openSourceScenario(sourceScenarioName);

        assertThat(evaluatePage.isCurrentScenarioNameDisplayed(sourceScenarioName), is(true));
    }

    @Test
    @Description("Validate the user can switch the source part")
    @TestRail(testCaseId = {"6467", "7873", "7874"})
    public void switchSourcePart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String source2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";
        String source2PartName = "Die Casting Lower Control Arm (Source1)";
        String twoModelPartName = "Die Casting Lower Control Arm (As Machined2)";
        currentUser = UserUtil.getUser();

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".SLDPRT");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, source2PartName + ".SLDPRT");
        twoModelFile2 = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModelPartName + ".SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(source2PartName, source2ScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile2, currentUser)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(sourceScenarioName)).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(12.31), Offset.offset(5.0));

        evaluatePage.selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(source2PartName)
            .highlightScenario(source2PartName, source2ScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(source2ScenarioName)).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(8.17), Offset.offset(3.0));

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2320")
    @Description("Validate the user cannot use two completely different CAD models")
    @TestRail(testCaseId = {"7871", "6630"})
    public void testTwoModelCorrectCADModels() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "casting_before_machining";
        String wrongSourcePartName = "PowderMetalShaft";
        String twoModelPartName = "casting_AFTER_machining";
        currentUser = UserUtil.getUser();

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, wrongSourcePartName + ".stp");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModelPartName + ".stp");

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
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(wrongSourcePartName)
            .highlightScenario(wrongSourcePartName, testScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isEqualTo(true);

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Costing Failed", "Units of the model of the stock differ from the units of the finished model.", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Units of the model of the stock differ from the units of the finished model.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7878"})
    @Description("Validate the select source button is not clickable in the public workspace")
    public void selectSourceButtonDisabledInPublicWorkspace() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final ProcessGroupEnum processGroupEnumTwoModel = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Raw Casting";
        String twoModelPartName = "Machined Casting";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".prt");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnumTwoModel, twoModelPartName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, sourceScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(processGroupEnumTwoModel)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, sourceScenarioName)
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .openScenario(sourcePartName, sourceScenarioName)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .openScenario(twoModelPartName, twoModelScenarioName)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(sourcePartName, sourceScenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(twoModelPartName, twoModelScenarioName)).isEqualTo(1);

        evaluatePage = explorePage.openScenario(twoModelPartName, twoModelScenarioName);

        softAssertions.assertThat(evaluatePage.isSelectSourceButtonEnabled()).isFalse();

        softAssertions.assertAll();
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
