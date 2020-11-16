package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class PsoEditTests extends TestBase {

    private CidLoginPage loginPage;
    private ProcessSetupOptionsPage processSetupOptionsPage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"761", "762", "763", "764"})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions()
            .selectDefinedValueDropdown("8")
            .selectOverrideNominalButton()
            .setOverride("0.4")
            .selectAddColorantButton()
            .selectMaterialDefinedButton()
            .setMaterialRegrindInput("0.3")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.getDefinedValueDropdown("8"), is(true));
        assertThat(processSetupOptionsPage.getNominalWallThicknessOverride(), is("0.40"));
        assertThat(processSetupOptionsPage.isAddColorantSelected(), is(true));
        assertThat(processSetupOptionsPage.getMaterialRegrind(), is("0.30"));
    }

    @Test
    @TestRail(testCaseId = {"771", "772", "773"})
    @Description("Die Casting edit PSO")
    public void dieCastPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting-Die.stp");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())

            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(5)
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectMoldMaterialDropdown("AISI P20")
            .selectPartToleranceDropdown("Low Tolerance +/-0.254 (+/-0.010\")")
            .closePanel()
            .costScenario(5)
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions();

        assertThat(processSetupOptionsPage.getOptimizeForMinimumCostSelected(), is(true));
        assertThat(processSetupOptionsPage.getMoldMaterial("AISI P20"), is(true));
        assertThat(processSetupOptionsPage.getSelectedPartTolerance("Low Tolerance +/-0.254 (+/-0.010\")"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"769", "770"})
    @Description("Sand Casting edit PSO")
    public void sandCastPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SandCast.x_t");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectMoldMaterialDropdown("Plastic")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions();

        assertThat(processSetupOptionsPage.getOptimizeForMinimumCostSelected(), is(true));
        assertThat(processSetupOptionsPage.getMoldMaterial("Plastic"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"768"})
    @Description("Machining - Validate the user can edit bundle sawing count")
    public void machiningPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions()
            .selectOverrideBundleCount()
            .setBundleCountOverride("3")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions();

        assertThat(processSetupOptionsPage.getBundleCount(), is("3"));
    }

    @Test
    @TestRail(testCaseId = {"774"})
    @Description("Powder Metal - Validate the user can edit the material allowance")
    public void powderMetalPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Compaction Pressing")
            .selectOptions()
            .selectMaterialAllowanceOverrideValue()
            .setMaterialAllowanceOverride("0.611")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Compaction Pressing")
            .selectOptions();

        assertThat(processSetupOptionsPage.getMaterialAllowance(), is("0.611"));
    }

    @Test
    @TestRail(testCaseId = {"775"})
    @Description("Sheet Plastic - Validate the user can edit the cooling time")
    public void sheetPlasticPSO() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "sheet_plastic.STEP");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("4 Cavities Drape Forming")
            .selectOptions()
            .setCoolingtimeInput("150.29")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("4 Cavities Drape Forming")
            .selectOptions();

        assertThat(processSetupOptionsPage.getCoolingTime(), is("150.29"));
    }

    @Test
    @TestRail(testCaseId = {"1652"})
    @Description("Validate user can change a selection of PSOs for a variety of routings in CI Design")
    public void routingPSOs() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "plasticLid.SLDPRT");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectOverrideNominalButton()
            .setOverride("0.13")
            .selectUserDefinedColorChargeButton()
            .setDefinedColorChargeInput("0.68")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.getOptimizeForMinimumCostSelected(), is(true));
        assertThat(processSetupOptionsPage.getNominalWallThicknessOverride(), is("0.13"));
        assertThat(processSetupOptionsPage.getColorChargeOverride(), is("0.68"));

        processSetupOptionsPage.closePanel()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Structural Foam Molding")
            .selectOptions()
            .selectDefinedValueDropdown("4")
            .selectAddColorantButton()
            .selectMaterialDefinedButton()
            .setMaterialRegrindInput("1.00")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Structural Foam Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.getDefinedValueDropdown("4"), is(true));
        assertThat(processSetupOptionsPage.isAddColorantSelected(), is(true));
        assertThat(processSetupOptionsPage.getMaterialRegrind(), is("1.00"));
    }

    @Test
    @TestRail(testCaseId = {"749"})
    @Description("Validate PSO Cannot be a junk value")
    public void junkPSO() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");

        loginPage = new CidLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions()
            .selectOverrideBundleCount()
            .setBundleCountOverride("jrigm")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions();

        assertThat(processSetupOptionsPage.getBundleCount(), is(""));
    }
}