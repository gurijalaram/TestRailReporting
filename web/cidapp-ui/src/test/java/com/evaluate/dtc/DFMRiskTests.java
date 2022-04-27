package com.evaluate.dtc;

import static com.apriori.utils.enums.ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
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
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("High"));
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
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.MEDIUM.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));
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
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.CRITICAL.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Critical"));
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
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
    }

    @Test
    @TestRail(testCaseId = {"6462"})
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
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario(3);

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.MEDIUM.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));
    }

    @Test
    @Issue("CIG-343")
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
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
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
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.MEDIUM.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        evaluatePage.selectProcessGroup(SHEET_METAL_TRANSFER_DIE)
            .costScenario();

        assertThat(evaluatePage.getDfmRisk(), is("-"));
    }

    @Test
    @Issue("BA-2315")
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
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario(5);

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/
    }

    @Test
    @Issue("BA-2315")
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
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario(3);

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.MEDIUM.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/

    }

    @Test
    @Issue("CIG-371")
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
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE), is(true));
        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/
    }

    @Test
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
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        softAssertions.assertAll();

        // TODO uncomment this section when revert is implemented
        /*evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));*/
    }

    @Test
    @Issue("CIG-343")
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
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.HIGH.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
        assertThat(evaluatePage.getDfmRiskIcon(), is(EvaluateDfmIconEnum.LOW.getIcon()));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        // TODO uncomment this section when revert is implemented
        /*
        evaluatePage.revert()
            .revertScenario();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High")), is(true));*/
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

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(639), Offset.offset(10.0));

        evaluatePage.updateCadFile(cadResourceFile)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 3);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isTrue();
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(372), Offset.offset(1.0));

        // TODO uncomment this section when revert is implemented
        /*evaluatePage.revert()
            .revertScenario();

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(744, 1)));*/
    }
}