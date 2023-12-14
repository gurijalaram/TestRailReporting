package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SecondaryProcessTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private AdvancedPage advancedPage;
    private MaterialProcessPage materialProcessPage;

    private SecondaryProcessesPage secondaryProcessPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public SecondaryProcessTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5140, 5115, 5132, 5444})
    @Description("Test secondary process leak test - edit wall thickness PSO and validate the process chart")
    public void secondaryProcessLeakTest() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Hydrostatic Leak Testing")
            .inputOptionsOverride("0.21")
            .submit(AdvancedPage.class)
            .openSecondaryDF()
            .usePrimaryDF("No")
            .selectDropdown("Other Secondary Processes", DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Hydrostatic Leak Testing");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Hydrostatic Leak Testing")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getAverageWallThickness()).isEqualTo(0.21);
        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(id = {5142, 5149})
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carburize")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Carburize");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectDropdown("Fully Burdened Cost")
            .selectBarChart("Carburize")
            .selectOptionsTab()
            .inputCaseDepth("0.46")
            .inputMasking("1")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectDropdown("Fully Burdened Cost")
            .selectBarChart("Melting")
            .selectBarChart("Carburize")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Case Depth Selection")).isEqualTo(0.46);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Masking")).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5151})
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Atmosphere Oil Harden")
            .inputMaskingOverride("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Atmosphere Oil Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(id = {5157})
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Standard Anneal")
            .inputMaskingOverride("1")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Standard Anneal");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectDropdown("Fully Burdened Cost")
            .selectBarChart("Standard Anneal")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Masking")).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5161, 8866})
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Vacuum Temper")
            .inputMaskingOverride("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree(" Heat Treat Processes, Temper")
            .highlightSecondaryProcess("Vacuum Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Stress Relief")
    public void secondaryProcessStressRelief() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("7075")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes")
            .selectSecondaryProcess("Stress Relief")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Stress Relief");

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Anodize")
    public void secondaryProcessAnodize() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Anodize");

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Certification")
    public void secondaryProcessCertification() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Certification")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Certification");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5132})
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Powder Coat Cart");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Powder Coat Cart");

        softAssertions.assertThat(materialProcessPage.getProcessPercentage("PowderCoat Cart")).contains("96.29%");

        softAssertions.assertAll();
    }

    @Test
    @Issues(@Issue("BA-2651"))
    @TestRail(id = {5141, 5142, 5143})
    @Description("Test secondary process powder coat cart PSO")
    public void psoPowderCoatCart() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .inputFractionPartArea("0.30")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Powder Coat Cart")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Fraction of Part Area that is Powder Coated")).isEqualTo(0.30);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5144, 5145, 5146, 5147})
    @Description("Test secondary process wet coat line PSO")
    public void psoWetCoatLine() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Wet Coat Line")
            .inputFractionOverride("0.40")
            .inputMaskedFeatures("10")
            .inputBatchSizeOverride("254")
            .inputCompLoadBar("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Wet Coat Line")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("What Fraction of Component is Painted?")).isEqualTo(0.40);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Number of Masked Features")).isEqualTo(10.0);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Number of Components Per Load Bar")).isEqualTo(1.0);

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Passivation")
    public void secondaryProcessPassivation() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Passivation");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5116, 5119})
    @Description("Multiple Secondary Processes before Costing")
    public void multiSecondaryProcessBeforeCost() {
        String filterName = new GenerateStringUtil().generateFilterName();
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, 440B")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Passivation / Carton Forming / Pack & Load");

        evaluatePage.publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, component.getScenarioName())
            .submit(ExplorePage.class)
            .openScenario("SheetMetal", component.getScenarioName())
            .goToAdvancedTab();

        softAssertions.assertThat(evaluatePage.isSecondaryProcessButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {5117})
    @Description("Multiple Secondary Processes after Costing")
    public void multiSecondaryProcessAfterCost() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Anodize / Carton Forming / Pack & Load");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(id = {5150})
    @Description("Test secondary process Carbonitride")
    public void secondaryProcessCarbonitride() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carbonitride")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .highlightSecondaryProcess("Carbonitride");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5152})
    @Description("Test secondary process Vacuum air harden")
    public void secondaryProcessVacuumAirHarden() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5153, 5162})
    @Description("Test secondary process Vacuum Air Harden with High Temper")
    public void secondaryProcessVacuumAirHardenHighTemp() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden with High Temper")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden with High Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5154})
    @Description("Test secondary process Spring steel")
    public void secondaryProcessSpringSteel() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Spring Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Spring Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5155})
    @Description("Test secondary process Stainless steel")
    public void secondaryProcessStainlessSteel() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Stainless Steel Harden")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Stainless Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5156})
    @Description("Test secondary process High Speed Steel Harden")
    public void secondaryProcessHighSpeedSteel() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("High Speed Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("High Speed Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5158})
    @Description("Test secondary process Low Temp Vacuum Anneal")
    public void secondaryProcessLowTempVacuumAnneal() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Low Temp Vacuum Anneal")
            .inputMasking("4")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("Low Temp Vacuum Anneal");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(4.0);
        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5159, 5129})
    @Description("Test secondary process High Temp Vacuum Anneal")
    public void secondaryProcessHighTempVacuumAnneal() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("High Temp Vacuum Anneal")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("High Temp Vacuum Anneal");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5160})
    @Description("Test secondary process Standard Temper")
    public void secondaryProcessStandardTemper() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Standard Temper")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .highlightSecondaryProcess("Standard Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5122})
    @Description("Selections are cleared when user cancels changes")
    public void selectionsCleared() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .cancel()
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5127})
    @Description("Validate if a secondary process fails to cost, entire part fails to cost")
    public void secondaryProcessCostFailed() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5133, 5134, 5138})
    @Description("Validate the user can clear all secondary process selections")
    public void clearAllSP() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .reset()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .reset()
            .cancel()
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");

        evaluatePage = advancedPage.openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .deselectAll()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .deselectAll()
            .cancel();

        softAssertions.assertThat(evaluatePage.getListOfSecondaryProcesses()).contains("No Processes Selected...");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5120, 5121, 5123})
    @Description("Validate zero count when no secondary process is selected and Test secondary process xray")
    public void secondaryProcessXray() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS_10_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");

        evaluatePage = advancedPage.openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Xray Inspection");

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("[Other Secondary Processes] Xray Inspection");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {9106, 9102})
    @Description("Check that Powder Coat Conveyor is available in Surface Treatment")
    public void secondaryProcessPowderCoatConveyor() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint, Conveyor Processes")
            .selectSecondaryProcess("Powder Coat Conveyor")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Conveyor Powder Coating");

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("[Surface Treatment] Powder Coat Conveyor");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8867})
    @Description("Validate the user can select a different secondary DF for each type of secondary process")
    public void secondaryProcessNotDefaultDigitalFactory() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToMachiningTab()
            .selectSecondaryProcess("Deburr")
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Heat Treat Processes")
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Vibratory Finishing")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Cleaning")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryDF()
            .usePrimaryDF("No")
            .selectDropdown("Heat Treatment", DigitalFactoryEnum.APRIORI_CHINA)
            .selectDropdown("Machining", DigitalFactoryEnum.APRIORI_MEXICO)
            .selectDropdown("Surface Treatment", DigitalFactoryEnum.APRIORI_INDIA)
            .selectDropdown("Other Secondary Processes", DigitalFactoryEnum.APRIORI_BRAZIL)
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab();

        assertThat(advancedPage.getListOfSecondaryDigitalFactory(), hasItems("[Heat Treatment] aPriori China", "[Machining] aPriori Mexico", "[Surface Treatment] aPriori India", "[Other Secondary Processes] aPriori Brazil"));
    }
}