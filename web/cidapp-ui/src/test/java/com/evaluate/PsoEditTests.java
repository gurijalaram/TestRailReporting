package com.evaluate;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class PsoEditTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7286", "7287", "7288", "7289", "6634", "6635"})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap DFM";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(8);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.40);
        softAssertions.assertThat(materialProcessPage.isColorantSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Material Regrind Allowance (Piece Part Cost Driver)")).isEqualTo(0.30);

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7269", "7297", "7289", "7296"})
    @Description("Die Casting edit PSO")
    public void dieCastPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting-Die";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
            .selectBarChart("High Pressure Die Casting")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getMoldMaterial()).isEqualTo("AISI P20");
        softAssertions.assertThat(materialProcessPage.getPartTolerance()).isEqualTo("Low Tolerance +/-0.254 (+/-0.010\")");

        materialProcessPage.selectNumberOfCavitiesDropdown("4")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("High Pressure Die Casting")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(4);

        softAssertions.assertAll();
    }

    @Ignore("Ignoring this test only as we need to find a way to count bar horizontally")
    @Test
    @TestRail(testCaseId = {"7294", "7295"})
    @Description("Sand Casting edit PSO")
    public void sandCastPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario(3)
            .openMaterialProcess()
            // TODO: 18/08/2021 cn - bar chart showing as double
            .selectBarChart("Vertical Automatic")
            .selectOptionsTab()
            .selectOptimizeMinCost()
            .selectMoldMaterial("Plastic")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Vertical Automatic")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getMoldMaterial()).isEqualTo("Plastic");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7293"})
    @Description("Machining - Validate the user can edit bundle sawing count")
    public void machiningPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab()
            .inputBundleCount("3")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Bundle Sawing"), is(3.0));
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7299"})
    @Description("Powder Metal - Validate the user can edit the material allowance")
    public void powderMetalPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Compaction Pressing")
            .selectOptionsTab()
            .inputMaterialAllowance("0.61")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Compaction Pressing")
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Material Allowance (Piece Part Cost Driver)"), is(0.61));
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7300"})
    @Description("Sheet Plastic - Validate the user can edit the cooling time")
    public void sheetPlasticPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        String componentName = "sheet_plastic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("4 Cavities Drape Forming")
            .selectOptionsTab()
            .inputCoolingTime("150.29")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("4 Cavities Drape Forming")
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Cooling Time"), is(150.29));
    }

    @Test
    @TestRail(testCaseId = {"8972"})
    @Description("Validate user can change a selection of PSOs for a variety of routings in CI Design")
    public void routingPSOs() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "plasticLid";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.13);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant   (Piece Part Cost Driver)")).isEqualTo(0.68);

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
            .selectBarChart("Structural Foam Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(4);
        softAssertions.assertThat(materialProcessPage.isColorantSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Material Regrind Allowance (Piece Part Cost Driver)")).isEqualTo(1.00);

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"7275"})
    @Description("Validate PSO Cannot be a junk value")
    public void junkPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab()
            .inputBundleCount("jrigm")
            .closePanel()
            .openMaterialProcess()
            .selectBarChart("Band Saw")
            .selectOptionsTab();

        assertThat(String.valueOf(materialProcessPage.getOverriddenPso("Bundle Sawing")), is(not(equalTo("jrigm"))));
    }

    @Test
    @Issue("BA-2744")
    @TestRail(testCaseId = {"16707"})
    @Description("Validate user can make iterative PSO changes and then re-cost to original defaults")
    public void multiplePSOEdits() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "plasticLid";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(0.52);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant   (Piece Part Cost Driver)")).isEqualTo(0.86);

        materialProcessPage.selectNumberOfCavitiesPiecePartToolingDropdown("32")
            .overrideWallThicknessPiecePart("2.52")
            .inputColorCharge("2.86")
            .closePanel()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(5.72), Offset.offset(0.2));

        evaluatePage.openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getDefinedValue()).isEqualTo(32);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness  (Piece Part Cost Driver)")).isEqualTo(2.52);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant   (Piece Part Cost Driver)")).isEqualTo(2.86);

        materialProcessPage.selectCavitiesDefaultValue()
            .selectWallThicknessDeriveFromPart()
            .selectNoColorant()
            .closePanel()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(0.81), Offset.offset(0.2));

        evaluatePage.openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesDefaultValueSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.isWallThicknessDeriveFromPartSelected()).isTrue();
        softAssertions.assertThat(materialProcessPage.isNoColorantSelected()).isTrue();

        softAssertions.assertAll();
    }
}