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

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));
    }
    @Test
    @TestRail(testCaseId = {"6454"})
    @Description("Validate DFM Risk - Medium for Stock Machining")
    public void stockMachiningMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, "9856874Medium.prt.1");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6456"})
    @Description("Validate DFM Risk - Critical for Sheet Metal")
    public void sheetMetalCriticalDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "1271576_CRITICAL.prt.1");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "3571050_cad.prt.1");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6462"})
    @Description("Validate DFM Risk - Medium Plastic Moulding")
    public void plasticMouldedMediumDFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "PlasticMoulded-Special Tooling Sliders Lifters.CATPart");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_SAND, "casting_q5_thinvalve.prt");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("High"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        evaluatePage.inputProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Unknown"), is(true));
        assertThat(evaluatePage.isDfmRisk("Unknown"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3862", "1239", "3863"})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedStockMachining() {

        final String fileName = "1379344.stp";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, fileName);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, fileName);

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3877", "3876"})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedPlasticMoulding() {

        final String file = "DTCPlasticIssues.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"3864", "3865"})
    @Description("Validate DFM Risk can be REDUCED for SHEET METAL")
    public void dfmReducedSheetMetal() {

        final String file = "bracketdfm.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"3872", "596", "3873"})
    @Description("Validate DFM Risk can be REDUCED for DIE CAST")
    public void dfmReducedDieCast() {

        final String file = "manifold.prt.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"3874", "3875"})
    @Description("Validate DFM Risk can be REDUCED for SAND CAST")
    public void dfmReducedSandCast() {

        final String file = "SandCastBox.SLDPRT";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"1245"})
    @Description("CAD file association can be updated & subsequently reverted")
    public void revertCADUpdate() {

        final String file = "1379344.stp";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, file);
        cadResourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, file);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(744, 1));

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