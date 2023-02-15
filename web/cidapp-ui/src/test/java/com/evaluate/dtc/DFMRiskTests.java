package com.evaluate.dtc;

import static com.apriori.utils.enums.ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.InvestigationPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.EvaluateDfmIconEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DFMRiskTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;

    private UserCredentials currentUser;
    private File resourceFile;
    private File cadResourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();

    public DFMRiskTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6453"})
    @Description("Validate DFM Risk - High for Stock Machining")
    public void stockMachiningHighDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "gs515625_gt077_high.prt.2");
        currentUser = UserUtil.getUser();
        String componentName = "gs515625_gt077_high";

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6454"})
    @Description("Validate DFM Risk - Medium for Stock Machining")
    public void stockMachiningMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "9856874Medium";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, componentName + ".prt.1");
        currentUser = UserUtil.getUser();


        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6456"})
    @Description("Validate DFM Risk - Critical for Sheet Metal")
    public void sheetMetalCriticalDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "1271576_CRITICAL";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6457"})
    @Description("Validate DFM Risk - High for Sheet Metal")
    public void sheetMetalHighDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "3571050_cad";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
    }

    @Test
    @TestRail(testCaseId = {"6462", "6415", "6416"})
    @Description("Validate DFM Risk - Medium Plastic Moulding")
    public void plasticMouldedMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulded-Special Tooling Sliders Lifters";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6484", "6485"})
    @Description("Validate when switch PG from a group with dfm risk to a group without that the risk is removed")
    public void noRiskTransferDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6472", "6824", "6473"})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedStockMachining() {
        final String fileName = "1379344.stp";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "1379344";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, fileName);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, fileName);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6480", "6481"})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedPlasticMoulding() {
        final String file = "DTCPlasticIssues.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "DTCPlasticIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6474", "6475"})
    @Description("Validate DFM Risk can be REDUCED for SHEET METAL")
    public void dfmReducedSheetMetal() {
        final String file = "bracketdfm.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracketdfm";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6476", "5439", "6477"})
    @Issue("APD-1663")
    @Description("Validate DFM Risk can be REDUCED for DIE CAST")
    public void dfmReducedDieCast() {
        final String file = "manifold.prt.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "manifold";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6478", "6479", "6471"})
    @Description("Validate DFM Risk can be REDUCED for SAND CAST")
    public void dfmReducedSandCast() {
        final String file = "SandCastBox.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCastBox";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
    @TestRail(testCaseId = {"6830"})
    @Description("CAD file association can be updated & subsequently reverted")
    public void revertCADUpdate() {
        final String file = "1379344.stp";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "1379344";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario(5);

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).as("Initial Fully Burdened Cost").isCloseTo(Double.valueOf(1135.79), Offset.offset(30.0));

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).as("Updated Fully Burdened Cost").isCloseTo(Double.valueOf(669.32), Offset.offset(30.0));

        // TODO uncomment this section when revert is implemented
        /*evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(744, 1)));*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12071"})
    @Description("Validate that Update CAD file is successful with Creo version components")
    public void updateCADCreoVersion() {
        final String cadFile = "bar_test1.prt";
        final String creoVersionFile = "bar_test1.prt.2";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "bar_test1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, cadFile);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, creoVersionFile);
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario(5);

        evaluatePage.clickActions()
            .updateCadFile(cadResourceFile);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6470"})
    @Description("Validate DFM Risk - Medium Sand Casting")
    public void sandCastingMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        softAssertions.assertAll();
    }
}