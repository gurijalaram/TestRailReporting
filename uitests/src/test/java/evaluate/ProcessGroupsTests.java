package test.java.evaluate;

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
import main.java.properties.reader.FileResourceReader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class ProcessGroupsTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public ProcessGroupsTests() {
        super();
    }

    @Test
    @Description("Testing process group Forging")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupForging() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Forging", new FileResourceReader().getResourceFile("case_001_006-8613190_2.prt.2"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Forging", "case_001_006-8613190_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Stock Machining")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupStockMachining() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Stock Machining", new FileResourceReader().getResourceFile("42x1021_ref.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Stock Machining", "42x1021_ref");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Bar and Tube")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupBarTube() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Bar and Tube", new FileResourceReader().getResourceFile("350611.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Bar and Tube", "350611");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Casting")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupCasting() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Casting", new FileResourceReader().getResourceFile("case_012_009-0020647_hinge_2.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Casting", "case_012_009-0020647_hinge_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Extrusion")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupExtrusion() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Extrusion", new FileResourceReader().getResourceFile("700-33770-01_A0.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Extrusion", "case_012_009-700-33770-01_A0");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Filleting")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupFilleting() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Filleting", new FileResourceReader().getResourceFile("case_005_flat end mill contouring.SLDPRT"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Filleting", "case_005_flat end mill contouring");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Gear Making")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupGearMaking() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Gear Making", new FileResourceReader().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Gear Making", "Case_001_-_Rockwell_2075-0243G");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Machining-Contouring")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningContouring() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Machining-Contouring", new FileResourceReader().getResourceFile("case_002_00400016-003M10_A.STP"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Machining-Contouring", "case_002_00400016-003M10_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Machining-Gage Parts")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningGageParts() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Machining-Gage Parts", new FileResourceReader().getResourceFile("GagePart_Case_011_gundrillgagepart-01.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Machining-Gage Parts", "GagePart_Case_011_gundrillgagepart-01");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Machining-Milling-4 Axis Mill")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachining4AxisMill() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Machining-Milling-4 Axis Mill", new FileResourceReader().getResourceFile("prt0001.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Machining-Milling-4 Axis Mill", "prt0001");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Machining-Milling-5 Axis Mill")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachining5AxisMill() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Machining-Milling-5 Axis Mill", new FileResourceReader().getResourceFile("7021021-2_rib.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Machining-Milling-5 Axis Mill", "7021021-2_rib");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Machining-Milling-Mill Turn")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupMachiningMillTurn() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Machining-Milling-Mill Turn", new FileResourceReader().getResourceFile("ms16555-627_1.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Machining-Milling-Mill Turn", "ms16555-627_1");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Partially Automated Machining")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPartiallyAutomatedMachining() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Partially Automated Machining", new FileResourceReader().getResourceFile("14100640.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Partially Automated Machining", "14100640");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Perimeter Milling")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPerimeterMilling() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Perimeter Milling", new FileResourceReader().getResourceFile("14100640.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Perimeter Milling", "14100640");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Pocket Recognition")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPocketRecognition() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Pocket Recognition", new FileResourceReader().getResourceFile("03229_0032_002_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Pocket Recognition", "03229_0032_002_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Pocket Recognition - shared walls")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPocketRecognitionSharedWalls() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Pocket Recognition - shared walls", new FileResourceReader().getResourceFile("case_066_SpaceX_00128711-001_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Pocket Recognition - shared walls", "case_066_SpaceX_00128711-001_A");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Rough Milling")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupRoughMilling() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Rough Milling", new FileResourceReader().getResourceFile("16-340053-00-04.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Rough Milling", "16-340053-00-04");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Slot Examples")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupSlotExamples() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Slot Examples", new FileResourceReader().getResourceFile("case_007_SpaceX_00088481-001_C.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Slot Examples", "case_007_SpaceX_00088481-001_C");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Turning")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupTurning() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Turning", new FileResourceReader().getResourceFile("case_002_006-8611543_prt.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Turning", "case_002_006-8611543_prt");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Blow Molding")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupBlowMolding() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Blow Molding", new FileResourceReader().getResourceFile("225_gasket-1-solid1.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Blow Molding", "225_gasket-1-solid1");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group PMI")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPMI() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("PMI", new FileResourceReader().getResourceFile("case_04_gtoldtc.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("PMI", "case_04_gtoldtc");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Powder Metal")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupPowderMetal() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Powder Metal", new FileResourceReader().getResourceFile("case_31_test_part_6_small.prt.2"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Powder Metal", "case_31_test_part_6_small");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Roll Bending")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupRollBending() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Roll Bending", new FileResourceReader().getResourceFile("AGCO _ 71421375.prt.1"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Roll Bending", "AGCO _ 71421375");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("Testing process group Sheet Metal-Transfer Die")
    @Severity(SeverityLevel.CRITICAL)
    public void testProcessGroupTransferDie() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Transfer Die", new FileResourceReader().getResourceFile("case_011_CENA-009-A1-LH-Rear-Body-Mount.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("Transfer Die", "case_011_CENA-009-A1-LH-Rear-Body-Mount");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.editScenario()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }
}