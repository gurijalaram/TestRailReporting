package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.enums.ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.InvestigationPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DFMRiskTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;

    private File cadResourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public DFMRiskTests() {
        super();
    }

    @Test
    @TestRail(id = {6453})
    @Description("Validate DFM Risk - High for Stock Machining")
    public void stockMachiningHighDFM() {

        component = new ComponentRequestUtil().getComponent("gs515625_gt077_high");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.HIGH.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6454})
    @Description("Validate DFM Risk - Medium for Stock Machining")
    public void stockMachiningMediumDFM() {
        component = new ComponentRequestUtil().getComponent("9856874Medium");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6456})
    @Description("Validate DFM Risk - Critical for Sheet Metal")
    public void sheetMetalCriticalDFM() {
        component = new ComponentRequestUtil().getComponent("1271576_CRITICAL");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.CRITICAL.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Critical");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6457})
    @Description("Validate DFM Risk - High for Sheet Metal")
    public void sheetMetalHighDFM() {
        component = new ComponentRequestUtil().getComponent("3571050_cad");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
    }

    @Test
    @TestRail(id = {6462, 6415, 6416})
    @Description("Validate DFM Risk - Medium Plastic Moulding")
    public void plasticMouldedMediumDFM() {
        component = new ComponentRequestUtil().getComponent("PlasticMoulded-Special Tooling Sliders Lifters");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(3);

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        investigationPage = new EvaluatePage(driver).openDesignGuidance()
            .openInvestigationTab()
            .selectTopic("Slides and Lifters");

        softAssertions.assertThat(investigationPage.getGcdCount("SlideBundle")).isEqualTo(2);
        softAssertions.assertThat(investigationPage.getGcdCount("LifterBundle")).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6484, 6485})
    @Description("Validate when switch PG from a group with dfm risk to a group without that the risk is removed")
    public void noRiskTransferDie() {
        component = new ComponentRequestUtil().getComponent("bracket_basic");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.selectProcessGroup(SHEET_METAL_TRANSFER_DIE)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("-");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6470})
    @Description("Validate DFM Risk - Medium Sand Casting")
    public void sandCastingMediumDFM() {
        component = new ComponentRequestUtil().getComponent("SandCast");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6472, 6824, 6473})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedStockMachining() {
        component = new ComponentRequestUtil().getCloudComponent("1379344");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(5);

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.HIGH.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        softAssertions.assertAll();

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6480, 6481})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedPlasticMoulding() {
        component = new ComponentRequestUtil().getCloudComponent("DTCPlasticIssues");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(3);

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.HIGH.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6474, 6475})
    @Description("Validate DFM Risk can be REDUCED for SHEET METAL")
    public void dfmReducedSheetMetal() {
        component = new ComponentRequestUtil().getCloudComponent("bracketdfm");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6476, 5439, 6477})
    @Description("Validate DFM Risk can be REDUCED for DIE CAST")
    public void dfmReducedDieCast() {
        component = new ComponentRequestUtil().getCloudComponent("manifold2");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        // TODO uncomment this section when revert is implemented
        /*evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6478, 6479, 6471})
    @Description("Validate DFM Risk can be REDUCED for SAND CAST")
    public void dfmReducedSandCast() {
        component = new ComponentRequestUtil().getCloudComponent("SandCastBox");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6830})
    @Description("CAD file association can be updated & subsequently reverted")
    public void revertCADUpdate() {
        component = new ComponentRequestUtil().getCloudComponent("1379344");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), component.getComponentName() + component.getExtension());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(5);

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).as("Initial Fully Burdened Cost").isCloseTo(Double.valueOf(756.32), Offset.offset(30.0));

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).as("Updated Fully Burdened Cost").isCloseTo(Double.valueOf(343.45), Offset.offset(30.0));

        // TODO uncomment this section when revert is implemented
        /*evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(744, 1)));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12071})
    @Description("Validate that Update CAD file is successful with Creo version components")
    public void updateCADCreoVersion() {
        final String creoVersionFile = "bar_test1.prt.2";

        component = new ComponentRequestUtil().getCloudComponent("bar_test1");
        cadResourceFile = FileResourceUtil.getCloudCadFile(component.getProcessGroup(), creoVersionFile);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(5);

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();

        softAssertions.assertAll();
    }
}