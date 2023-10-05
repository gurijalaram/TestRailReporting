package com.apriori.evaluate;

import static com.apriori.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.enums.ProcessGroupEnum.ROTO_BLOW_MOLDING;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.EvaluateDfmIconEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ProcessGroupsTests extends TestBaseUI {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();

    public ProcessGroupsTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5441, 6631, 6632})
    @Description("Testing process group Forging")
    public void testProcessGroupForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "case_001_006-8613190_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.2");
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

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Hammer");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6123})
    @Description("Testing process group Stock Machining")
    public void testProcessGroupStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "42x1021_ref";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
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

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("2 Axis Lathe");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Testing process group Bar and Tube")
    @TestRail(id = {6124})
    public void testProcessGroupBarTube() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        String componentName = "350611";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Material Stock / Band Saw");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Testing process group Casting")
    @TestRail(id = {6125})
    public void testProcessGroupDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "case_012_009-0020647_hinge_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("High Pressure Die Casting");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Extrusion")
    @TestRail(id = {6126, 6461})
    public void testProcessGroupExtrusion() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Injection Molding");
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Filleting")
    @TestRail(id = {6127})
    public void testProcessGroupFilleting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_005_flat end mill contouring";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Test
    @Description("Testing process group Gear Making")
    @TestRail(id = {6128})
    public void testProcessGroupGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Case_001_-_Rockwell_2075-0243G";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Lathe");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Machining-Contouring")
    @TestRail(id = {6129})
    public void testProcessGroupMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_002_00400016-003M10_A";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STP");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("4 Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Testing process group Machining-Gage Parts")
    @TestRail(id = {6130})
    public void testProcessGroupMachiningGageParts() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        String componentName = "GagePart_Case_011_gundrillgagepart-01";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Machining-Milling-4 Axis Mill")
    @TestRail(id = {6132})
    public void testProcessGroupMachining4AxisMill() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "prt0001";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("2 Axis Lathe");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Machining-Milling-5 Axis Mill")
    @TestRail(id = {6131})
    public void testProcessGroupMachining5AxisMill() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "7021021-2_rib";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(5);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("4 Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Machining-Milling-Mill Turn")
    @TestRail(id = {6133})
    public void testProcessGroupMachiningMillTurn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "ms16555-627_1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("2 Axis Lathe");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Partially Automated Machining")
    @TestRail(id = {6134})
    public void testProcessGroupPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "14100640";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Axis Mill"));
    }

    @Test
    @Description("Testing process group Perimeter Milling")
    @TestRail(id = {6135})
    public void testProcessGroupPerimeterMilling() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "14100640";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Pocket Recognition - shared walls")
    @TestRail(id = {6136})
    public void testProcessGroupPocketRecognitionSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_066_SpaceX_00128711-001_A";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("5 Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Rough Milling")
    @TestRail(id = {6137})
    public void testProcessGroupRoughMilling() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "16-340053-00-04";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Mill");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Slot Examples")
    @TestRail(id = {6138})
    public void testProcessGroupSlotExamples() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_007_SpaceX_00088481-001_C";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Lathe"));
    }

    @Test
    @Description("Testing process group Turning")
    @TestRail(id = {6139})
    public void testProcessGroupTurning() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_002_006-8611543_prt";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("2 Axis Lathe");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Roto and Blow Molding")
    @TestRail(id = {6061, 8336})
    public void testProcessGroupBlowMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "225_gasket-1-solid1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(ROTO_BLOW_MOLDING)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.POLYETHYLENE_HDPE.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Rotational Mold");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Testing process group Powder Metal")
    @TestRail(id = {6142})
    public void testProcessGroupPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "case_31_test_part_6_small";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.2");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Furnace Sintering");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Testing process group Plasma Cut")
    @TestRail(id = 6141)
    public void testProcessGroupPlasmaCut() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "1271576_CRITICAL";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.CRITICAL.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Critical");
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Plasma Cut");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Sheet Metal-Transfer Die")
    @TestRail(id = {6143})
    public void testSheetMetalFiberLaser() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "case_011_CENA-009-A1-LH-Rear-Body-Mount";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Fiber Laser Cut");

        softAssertions.assertAll();
    }
}