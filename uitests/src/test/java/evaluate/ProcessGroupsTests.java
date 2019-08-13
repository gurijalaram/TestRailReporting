package test.java.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class ProcessGroupsTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public ProcessGroupsTests() {
        super();
    }

    @Test
    @Description("Testing process group Forging")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupForging() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_001_006-8613190_2.prt.2"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_001_006-8613190_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Stock Machining")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupStockMachining() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("42x1021_ref.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "42x1021_ref");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Bar and Tube")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupBarTube() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("350611.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "350611");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Casting")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupCasting() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_012_009-0020647_hinge_2.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_012_009-0020647_hinge_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Extrusion")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupExtrusion() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("700-33770-01_A0.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_012_009-700-33770-01_A0");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Filleting")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupFilleting() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_005_flat end mill contouring.SLDPRT"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_005_flat end mill contouring");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Gear Making")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupGearMaking() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "Case_001_-_Rockwell_2075-0243G");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Machining-Contouring")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningContouring() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_002_00400016-003M10_A.STP"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_002_00400016-003M10_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Machining-Gage Parts")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningGageParts() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("GagePart_Case_011_gundrillgagepart-01.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "GagePart_Case_011_gundrillgagepart-01");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Machining-Milling-4 Axis Mill")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachining4AxisMill() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("prt0001.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "prt0001");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Machining-Milling-5 Axis Mill")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachining5AxisMill() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("7021021-2_rib.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "7021021-2_rib");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Machining-Milling-Mill Turn")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningMillTurn() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("ms16555-627_1.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "ms16555-627_1");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Partially Automated Machining")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPartiallyAutomatedMachining() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("14100640.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "14100640");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Perimeter Milling")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPerimeterMilling() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("14100640.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "14100640");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Pocket Recognition")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPocketRecognition() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("03229_0032_002_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "03229_0032_002_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Pocket Recognition - shared walls")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPocketRecognitionSharedWalls() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_066_SpaceX_00128711-001_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_066_SpaceX_00128711-001_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Rough Milling")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupRoughMilling() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("16-340053-00-04.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "16-340053-00-04");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Slot Examples")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupSlotExamples() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_007_SpaceX_00088481-001_C.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_007_SpaceX_00088481-001_C");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Turning")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupTurning() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_002_006-8611543_prt.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_002_006-8611543_prt");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Blow Molding")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupBlowMolding() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("225_gasket-1-solid1.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "225_gasket-1-solid1");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group PMI")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPMI() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_04_gtoldtc.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_04_gtoldtc");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Powder Metal")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPowderMetal() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_31_test_part_6_small.prt.2"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_31_test_part_6_small");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Roll Bending")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupRollBending() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("AGCO _ 71421375.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "AGCO _ 71421375");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Testing process group Sheet Metal-Transfer Die")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupTransferDie() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_011_CENA-009-A1-LH-Rear-Body-Mount.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "case_011_CENA-009-A1-LH-Rear-Body-Mount");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(),is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }
}