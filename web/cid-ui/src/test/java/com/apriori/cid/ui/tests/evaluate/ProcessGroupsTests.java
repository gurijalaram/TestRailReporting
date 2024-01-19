package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.shared.util.enums.ProcessGroupEnum.ROTO_BLOW_MOLDING;
import static com.apriori.shared.util.enums.ProcessGroupEnum.STOCK_MACHINING;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ProcessGroupsTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public ProcessGroupsTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5441, 6631, 6632})
    @Description("Testing process group Forging")
    public void testProcessGroupForging() {
        component = new ComponentRequestUtil().getComponent("case_001_006-8613190_2");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("42x1021_ref");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("350611");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("case_012_009-0020647_hinge_2");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponentWithProcessGroup("700-33770-01_A0", PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("case_005_flat end mill contouring");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("Case_001_-_Rockwell_2075-0243G");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("case_002_00400016-003M10_A");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("GagePart_Case_011_gundrillgagepart-01");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("prt0001");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("7021021-2_rib");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("ms16555-627_1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @TestRail(id = {6134, 6135})
    public void testProcessGroupPartiallyAutomatedMachining() {
        component = new ComponentRequestUtil().getComponent("14100640");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("case_066_SpaceX_00128711-001_A");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("16-340053-00-04");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("case_007_SpaceX_00088481-001_C");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponentWithProcessGroup("case_002_006-8611543_prt", STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("225_gasket-1-solid1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
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
        component = new ComponentRequestUtil().getComponent("case_31_test_part_6_small");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Plasma Cut");

        softAssertions.assertAll();
    }

    @Test
    @Description("Testing process group Sheet Metal-Transfer Die")
    @TestRail(id = {6143})
    public void testSheetMetalFiberLaser() {
        component = new ComponentRequestUtil().getComponent("case_011_CENA-009-A1-LH-Rear-Body-Mount");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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