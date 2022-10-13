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
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class PsoEditTests extends TestBase {

    private CidAppLoginPage loginPage;
    private MaterialProcessPage materialProcessPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
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
            .selectDefinedValue("8")
            .overrideWallThickness("0.4")
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
    @TestRail(testCaseId = {"7269", "7297", "7289"})
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
            .inputMaterialAllowance("0.611")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Compaction Pressing")
            .selectOptionsTab();

        assertThat(materialProcessPage.getOverriddenPso("Material Allowance (Piece Part Cost Driver)"), is(0.611));
    }

    @Test
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
    //TODO cn to uncomment this
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
            .overrideWallThickness("0.13")
            .inputColorCharge("0.68")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Injection Molding")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.isCavitiesOptimizeMinCostSelected()).isEqualTo(true);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness (Piece Part Cost Driver)")).isEqualTo(0.13);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Colorant (Piece Part Cost Driver)")).isEqualTo(0.68);
        softAssertions.assertAll();

        materialProcessPage.closePanel()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario(1)
            .openMaterialProcess()
            .selectBarChart("Structural Foam Molding")
            .selectOptionsTab()
            //.selectDefinedValueDropdown("4")
            .selectAddColorantButton()
            //.selectMaterialDefinedButton()
            //.setMaterialRegrindInput("1.00")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Structural Foam Molding")
            .selectOptionsTab();

        /*assertThat(materialProcessPage.getDefinedValueDropdown("4"), is(true));
        assertThat(materialProcessPage.isAddColorantSelected(), is(true));
        assertThat(materialProcessPage.getMaterialRegrind(), is("1.00"));*/
    }

    @Test
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
}