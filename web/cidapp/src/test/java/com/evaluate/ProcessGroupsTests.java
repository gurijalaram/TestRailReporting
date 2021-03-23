package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ProcessGroupsTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ProcessGroupsTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = "5441")
    @Description("Testing process group Forging")
    public void testProcessGroupForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_001_006-8613190_2.prt.2");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Hammer"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = "6123")
    @Description("Testing process group Stock Machining")
    public void testProcessGroupStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "42x1021_ref.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("2 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Bar and Tube")
    @TestRail(testCaseId = {"6124"})
    public void testProcessGroupBarTube() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "350611.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Casting")
    @TestRail(testCaseId = "6125")
    public void testProcessGroupDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_012_009-0020647_hinge_2.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("High Pressure Die Casting"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Extrusion")
    @TestRail(testCaseId = "6126")
    public void testProcessGroupExtrusion() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "700-33770-01_A0.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Injection Molding"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Filleting")
    @TestRail(testCaseId = "6127")
    public void testProcessGroupFilleting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_005_flat end mill contouring.SLDPRT");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Gear Making")
    @TestRail(testCaseId = "6128")
    public void testProcessGroupGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Case_001_-_Rockwell_2075-0243G.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Machining-Contouring")
    @TestRail(testCaseId = "6129")
    public void testProcessGroupMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_00400016-003M10_A.STP");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("4 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Machining-Gage Parts")
    @TestRail(testCaseId = "6130")
    public void testProcessGroupMachiningGageParts() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "GagePart_Case_011_gundrillgagepart-01.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Machining-Milling-4 Axis Mill")
    @TestRail(testCaseId = "6132")
    public void testProcessGroupMachining4AxisMill() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "prt0001.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("2 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Machining-Milling-5 Axis Mill")
    @TestRail(testCaseId = "6131")
    public void testProcessGroupMachining5AxisMill() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "7021021-2_rib.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(5);

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("4 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Machining-Milling-Mill Turn")
    @TestRail(testCaseId = "6133")
    public void testProcessGroupMachiningMillTurn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "ms16555-627_1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("2 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Partially Automated Machining")
    @TestRail(testCaseId = "6134")
    public void testProcessGroupPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "14100640.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Perimeter Milling")
    @TestRail(testCaseId = "6135")
    public void testProcessGroupPerimeterMilling() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "14100640.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Pocket Recognition - shared walls")
    @TestRail(testCaseId = "6136")
    public void testProcessGroupPocketRecognitionSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_066_SpaceX_00128711-001_A.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("5 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Rough Milling")
    @TestRail(testCaseId = "6137")
    public void testProcessGroupRoughMilling() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "16-340053-00-04.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Slot Examples")
    @TestRail(testCaseId = "6138")
    public void testProcessGroupSlotExamples() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_007_SpaceX_00088481-001_C.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Turning")
    @TestRail(testCaseId = "6139")
    public void testProcessGroupTurning() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_006-8611543_prt.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("2 Axis Lathe"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Roto and Blow Molding")
    @TestRail(testCaseId = {"6061"})
    public void testProcessGroupBlowMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Rotational Mold"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Powder Metal")
    @TestRail(testCaseId = "6142")
    public void testProcessGroupPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_31_test_part_6_small.prt.2");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Furnace Sintering"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Roll Bending")
    @TestRail(testCaseId = {"6141", "6144"})
    public void testProcessGroupRollBending() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS.SLDPRT");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDfmRiskIcon("Medium"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Plasma Cut"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Description("Testing process group Sheet Metal-Transfer Die")
    @TestRail(testCaseId = "6143")
    public void testSheetMetalFiberLaser() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_011_CENA-009-A1-LH-Rear-Body-Mount.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Fiber Laser Cut"));
    }
}