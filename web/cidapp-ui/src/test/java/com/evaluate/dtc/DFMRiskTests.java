package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
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

public class DFMRiskTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private UserCredentials currentUser;
    private File resourceFile;
    private File cadResourceFile;

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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Critical"), is(true));
        assertThat(evaluatePage.isDfmRisk("Critical"), is(true));
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6462"})
    @Description("Validate DFM Risk - Medium Plastic Moulding")
    public void plasticMouldedMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulded-Special Tooling Sliders Lifters";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, componentName +  ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6471"})
    @Description("Validate DFM Risk - Low for Sand Casting")
    public void sandCastLowDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "casting_q5_thinvalve";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_SAND, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Low"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        evaluatePage.inputProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Unknown"), is(true));
        assertThat(evaluatePage.isDfmRisk("Unknown"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario(5);

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        // TODO uncomment this section when update cad file is implemented
        /*        evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));*/
    }

    @Test
    @Category(SmokeTests.class)
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        // TODO uncomment this section when update cad file is implemented
        /*        evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));*/

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
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), cadResourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        // TODO uncomment this section when update cad file is implemented
        /*        evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_INCOMPLETE.getCostingText()), is(true));
        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6476", "5439", "6477"})
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        // TODO uncomment this section when update cad file is implemented
        /*evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6478", "6479"})
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        // TODO uncomment this section when update cad file is implemented
        /*        evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));*/
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario(5);

        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(640, 1));

        // TODO uncomment this section when update cad file is implemented
        /*evaluatePage.updateCadFile(cadResourceFile);
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(372, 1)));

        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(744, 1)));*/
    }
}