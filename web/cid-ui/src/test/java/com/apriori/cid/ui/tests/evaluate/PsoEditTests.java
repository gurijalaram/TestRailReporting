package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.enums.ProcessGroupEnum.CASTING_DIE;
import static com.apriori.shared.util.enums.ProcessGroupEnum.CASTING_SAND;
import static com.apriori.shared.util.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.shared.util.enums.ProcessGroupEnum.POWDER_METAL;
import static com.apriori.shared.util.enums.ProcessGroupEnum.STOCK_MACHINING;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PsoEditTests extends TestBaseUI {

    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private ComponentInfoBuilder component;

    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {7286, 7287, 7288, 7289, 6634, 6635})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(PLASTIC_MOLDING);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(3)
            .openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab()
            .selectNumberOfCavitiesPiecePartToolingDropdown("8")
            .overrideWallThicknessPiecePart("0.4")
            .selectAddColorantButton()
            .inputMaterialRegrind("0.3")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(8);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.40);
        softAssertions.assertThat(materialProcessPage.isColorantSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Material Regrind Allowance (%) (Piece Part Cost Driver)")).isEqualTo(0.30);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7269, 7297, 7289, 7296})
    @Description("Die Casting edit PSO")
    public void dieCastPSO() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(CASTING_DIE);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(7)
            .openMaterialProcess()
            .selectBarChart("High Pressure Die Casting")
            .selectOptionsTab()
            .selectOptimizeMinCost()
            .selectMoldMaterial("AISI P20")
            .selectTolerances("Low Tolerance +/-0.254 (+/-0.010\")")
            .closePanel()
            .costScenario(7)
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getMoldMaterial()).isEqualTo("AISI P20");
        softAssertions.assertThat(materialProcessPage.getPartTolerance()).isEqualTo("Low Tolerance +/-0.254 (+/-0.010\")");

        materialProcessPage.selectNumberOfCavitiesDropdown("1")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7294, 7295})
    @Description("Sand Casting edit PSO")
    public void sandCastPSO() {
        component = new ComponentRequestUtil().getComponent("SandCast");

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(3)
            .openMaterialProcess()
            .selectBarChart("Vertical Automatic")
            .selectOptionsTab()
            .selectOptimizeMinCost()
            .selectMoldMaterial("Plastic")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getMoldMaterial()).isEqualTo("Plastic");

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7293})
    @Description("Machining - Validate the user can edit bundle sawing count")
    public void machiningPSO() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(STOCK_MACHINING);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab()
            .inputBundleCount("3")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Bundle Sawing"), is(3.0));
    }

    @Test
    @TestRail(id = {7299})
    @Description("Powder Metal - Validate the user can edit the material allowance")
    public void powderMetalPSO() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(POWDER_METAL);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Compaction Pressing")
            .selectOptionsTab()
            .inputMaterialAllowance("0.61")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Material Allowance (Piece Part Cost Driver)"), is(0.61));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {8972})
    @Description("Validate user can change a selection of PSOs for a variety of routings in CI Design")
    public void routingPSOs() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(PLASTIC_MOLDING);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab()
            .selectCavitiesOptimizeMinCost()
            .overrideWallThicknessPiecePart("0.13")
            .inputColorCharge("0.68")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.13);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant (Piece Part Cost Driver)")).isEqualTo(0.68);

        materialProcessPage.closePanel()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario(1)
            .openMaterialProcess()
            .selectBarChart("Structural Foam Molding")
            .selectOptionsTab()
            .selectNumberOfCavitiesPiecePartToolingDropdown("4")
            .selectAddColorantButton()
            .inputMaterialRegrind("1.00")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(4);
        softAssertions.assertThat(materialProcessPage.isColorantSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Material Regrind Allowance (%) (Piece Part Cost Driver)")).isEqualTo(1.00);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7275})
    @Description("Validate PSO Cannot be a junk value")
    public void junkPSO() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(STOCK_MACHINING);

        materialProcessPage =  new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab()
            .inputBundleCount("jrigm")
            .closePanel()
            .openMaterialProcess()
            .selectOptionsTab();

        assertThat(String.valueOf(materialProcessPage.getOverriddenPso("Bundle Sawing")), is(not(equalTo("jrigm"))));
    }

    @Test
    @TestRail(id = {16707})
    @Description("Validate user can make iterative PSO changes and then re-cost to original defaults")
    public void multiplePSOEdits() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(PLASTIC_MOLDING);

        evaluatePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .openMaterialSelectorTable()
            .search("ABS")
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(0.81), Offset.offset(0.2));

        evaluatePage.openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab()
            .selectCavitiesOptimizeMinCost()
            .overrideWallThicknessPiecePart("0.52")
            .inputColorCharge("0.86")
            .closePanel()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(0.82), Offset.offset(0.2));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.52);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant (Piece Part Cost Driver)")).isEqualTo(0.86);

        materialProcessPage.selectNumberOfCavitiesPiecePartToolingDropdown("32")
            .overrideWallThicknessPiecePart("2.52")
            .inputColorCharge("2.86")
            .closePanel()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(5.51), Offset.offset(0.2));

        evaluatePage.openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(32);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(2.52);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant (Piece Part Cost Driver)")).isEqualTo(2.86);

        materialProcessPage.selectCavitiesDefaultValue()
            .selectWallThicknessDeriveFromPart()
            .selectNoColorant()
            .closePanel()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(0.81), Offset.offset(0.2));

        evaluatePage.openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesDefaultValueSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.isWallThicknessDeriveFromPartSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.isNoColorantSelected()).isTrue();

        softAssertions.assertAll();
    }
}