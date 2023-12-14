package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TwoModelMachiningTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;

    private File resourceFile;
    private File twoModelFile;
    private File twoModelFile2;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder component;
    private GuidanceIssuesPage guidanceIssuesPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @Description("Validate Source and util tile appears when 2 MM is selected")
    @TestRail(id = {7861, 7862, 7863, 7864, 7870})
    public void testTwoModelMachining() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("casting_BEFORE_machining");
        ComponentInfoBuilder twoModelPart = new ComponentRequestUtil().getComponent("casting_AFTER_machining");
        twoModelPart.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(sourcePart.getUser())
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPart)
            .selectProcessGroup(twoModelPart.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getSourceModelMaterial()).isEqualTo(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName());
        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(sourcePart.getScenarioName())).isEqualTo(true);

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
    @TestRail(id = {6466, 7866, 12511})
    public void testOpenSourceModel() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("VulcainCasting");
        ComponentInfoBuilder twoModelPart = new ComponentRequestUtil().getComponent("VulcainMachined");
        twoModelPart.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModelPart)
            .selectProcessGroup(twoModelPart.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario(5)
            .openSourceScenario(sourcePart.getScenarioName());

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(sourcePart.getScenarioName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(id = {7865, 7869, 7872})
    public void multiLevel2Model() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("2modeltest-cast");
        ComponentInfoBuilder twoModel = new ComponentRequestUtil().getComponent("2modeltest-machine1");
        twoModel.setUser(sourcePart.getUser());
        ComponentInfoBuilder twoModel2 = new ComponentRequestUtil().getComponent("2modeltest-machine2");
        twoModel2.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(sourcePart.getUser())
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(96.34), Offset.offset(5.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(18.88), Offset.offset(5.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(2.33), Offset.offset(5.0));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel)
            .selectProcessGroup(twoModel.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(82.70), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(28.2), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(1.93), Offset.offset(10.0));

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(twoModel2)
            .selectProcessGroup(twoModel2.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(twoModel.getComponentName())
            .highlightScenario(twoModel.getComponentName(), twoModel.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessesResult("Utilization")).isCloseTo(Double.valueOf(83.78), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(30.49), Offset.offset(10.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Finish Mass")).isCloseTo(Double.valueOf(1.62), Offset.offset(10.0));

        softAssertions.assertAll();
    }

    @Test
    @Description("Validate the User can open a public source part in the evaluate tab")
    @TestRail(id = {7867, 7876})
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
    @Tag(EXTENDED_REGRESSION)
    @Description("Validate the user can switch the source part")
    @TestRail(id = {6467, 7873, 7874})
    public void switchSourcePart() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("Die Casting Lower Control Arm (As Cast)");
        ComponentInfoBuilder sourcePart2 = new ComponentRequestUtil().getComponent("Die Casting Lower Control Arm (Source1)");
        sourcePart2.setUser(sourcePart.getUser());
        ComponentInfoBuilder twoModel = new ComponentRequestUtil().getComponent("Die Casting Lower Control Arm (As Machined2)");
        twoModel.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(sourcePart.getUser())
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        evaluatePage.clickExplore()
            .uploadComponentAndOpen(sourcePart2)
            .selectProcessGroup(sourcePart2.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModel)
            .selectProcessGroup(twoModel.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(sourcePart.getScenarioName())).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(12.31), Offset.offset(5.0));

        evaluatePage.selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart2.getComponentName())
            .highlightScenario(sourcePart2.getComponentName(), sourcePart2.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isSourcePartDetailsDisplayed(sourcePart2.getScenarioName())).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(8.17), Offset.offset(3.0));

        softAssertions.assertAll();
    }

    @Test
    @Description("Validate the user cannot use two completely different CAD models")
    @TestRail(id = {7871, 6630})
    public void testTwoModelCorrectCADModels() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("casting_BEFORE_machining");
        ComponentInfoBuilder wrongSourcePart = new ComponentRequestUtil().getComponent("PowderMetalShaft");
        wrongSourcePart.setUser(sourcePart.getUser());
        ComponentInfoBuilder twoModelPart = new ComponentRequestUtil().getComponent("casting_AFTER_machining");
        twoModelPart.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(wrongSourcePart)
            .selectProcessGroup(wrongSourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPart)
            .selectProcessGroup(twoModelPart.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(wrongSourcePart.getComponentName())
            .highlightScenario(wrongSourcePart.getComponentName(), wrongSourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isEqualTo(true);

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Costing Failed", "Units of the model of the stock differ from the units of the finished model.", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Units of the model of the stock differ from the units of the finished model.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7878})
    @Description("Validate the select source button is not clickable in the public workspace")
    public void selectSourceButtonDisabledInPublicWorkspace() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("Raw Casting");
        ComponentInfoBuilder twoModelPart = new ComponentRequestUtil().getComponent("Machined Casting");
        twoModelPart.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(sourcePart.getUser())
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPart)
            .selectProcessGroup(twoModelPart.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .openScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .openScenario(twoModelPart.getComponentName(), twoModelPart.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(sourcePart.getComponentName(), sourcePart.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(twoModelPart.getComponentName(), twoModelPart.getScenarioName())).isEqualTo(1);

        evaluatePage = explorePage.openScenario(twoModelPart.getComponentName(), twoModelPart.getScenarioName());

        softAssertions.assertThat(evaluatePage.isSelectSourceButtonEnabled()).isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7877})
    @Description("Validate the status icon updates if the source component is updated")
    public void updateSourceModel() {
        ComponentInfoBuilder sourcePart = new ComponentRequestUtil().getComponent("Raw Casting");
        ComponentInfoBuilder twoModelPart = new ComponentRequestUtil().getComponent("Machined Casting");
        twoModelPart.setUser(sourcePart.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(sourcePart.getUser())
            .uploadComponentAndOpen(sourcePart)
            .selectProcessGroup(sourcePart.getProcessGroup())
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPart)
            .selectProcessGroup(twoModelPart.getProcessGroup())
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePart.getComponentName())
            .highlightScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.clickExplore()
            .openScenario(sourcePart.getComponentName(), sourcePart.getScenarioName())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .clickCostButton()
            .clickExplore()
            .openScenario(twoModelPart.getComponentName(), twoModelPart.getScenarioName())
            .closeMessagePanel();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UNCOSTED_CHANGES)).isEqualTo(true);

        softAssertions.assertAll();
    }

    /*@Test
    @Description("Validate the user can fix the source scenario but selecting the continue button to return to the part")
    @TestRail(id = {7879"})
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
    @TestRail(id = {7879"})
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
