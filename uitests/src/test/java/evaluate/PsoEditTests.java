package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class PsoEditTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ProcessSetupOptionsPage processSetupOptionsPage;
    private ProcessRoutingPage processRoutingPage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"761", "762", "763", "764"})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions()
            .selectDefinedValueDropdown("8")
            .selectOverrideNominalButton()
            .setOverride("0.4")
            .selectAddColorantButton()
            .selectMaterialDefinedButton()
            .setMaterialRegrindInput("0.3");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.getDefinedValueDropdown("8"), is(true));
        assertThat(processSetupOptionsPage.isNominalWallThicknessOverride("0.4"), is(true));
        assertThat(processSetupOptionsPage.isAddColorantSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.isMaterialRegrind("0.3"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"771", "772", "773"})
    @Description("Die Casting edit PSO")
    public void dieCastPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting-Die.stp");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectMoldMaterialDropdown("AISI P20")
            .selectPartToleranceDropdown("Low Tolerance +/-0.254 (+/-0.010\")");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions();

        assertThat(processSetupOptionsPage.isOptimizeForMinimumCostSelected("checked"), is(true));
        assertThat(processSetupOptionsPage.getMoldMaterial("AISI P20"), is(true));
        assertThat(processSetupOptionsPage.getSelectedPartTolerance("Low Tolerance +/-0.254 (+/-0.010\")"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"769", "770"})
    @Description("Sand Casting edit PSO")
    public void sandCastPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("SandCast.x_t");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectMoldMaterialDropdown("Plastic");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions();

        assertThat(processSetupOptionsPage.isOptimizeForMinimumCostSelected("checked"), is(true));
        assertThat(processSetupOptionsPage.getMoldMaterial("Plastic"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"768"})
    @Description("Machining - Validate the user can edit bundle sawing count")
    public void machiningPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions()
            .selectOverrideBundleCount()
            .setBundleCountOverride("3");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions();

        assertThat(processSetupOptionsPage.isBundleCount("3"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"774"})
    @Description("Powder Metal - Validate the user can edit the material allowance")
    public void powderMetalPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Compaction Pressing")
            .selectOptions()
            .selectMaterialAllowanceOverrideValue()
            .setMaterialAllowanceOverride("0.611");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Compaction Pressing")
            .selectOptions();

        assertThat(processSetupOptionsPage.isMaterialAllowance("0.611"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"775"})
    @Description("Sheet Plastic - Validate the user can edit the cooling time")
    public void sheetPlasticPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("sheet_plastic.STEP");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("4 Cavities Drape Forming")
            .selectOptions()
            .setCoolingtimeInput("150.29");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("4 Cavities Drape Forming")
            .selectOptions();

        assertThat(processSetupOptionsPage.isCoolingTime("150.29"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1652"})
    @Description("Validate user can change a selection of PSOs for a variety of routings in CI Design")
    public void routingPSOs() {

        resourceFile = new FileResourceUtil().getResourceFile("plasticLid.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions()
            .selectOptimizeForMinimumCostButton()
            .selectOverrideNominalButton()
            .setOverride("0.13")
            .selectUserDefinedColorChargeButton()
            .setDefinedColorChargeInput("0.68");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.isOptimizeForMinimumCostSelected("checked"), is(true));
        assertThat(processSetupOptionsPage.isNominalWallThicknessOverride("0.13"), is(true));
        assertThat(processSetupOptionsPage.isColorChargeOverride("0.68"), is(true));

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Structural Foam Molding")
            .selectOptions()
            .selectDefinedValueDropdown("4")
            .selectAddColorantButton()
            .selectMaterialDefinedButton()
            .setMaterialRegrindInput("1.00");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Structural Foam Molding")
            .selectOptions();

        assertThat(processSetupOptionsPage.getDefinedValueDropdown("4"), is(true));
        assertThat(processSetupOptionsPage.isAddColorantSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.isMaterialRegrind("1.00"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"749"})
    @Description("Validate PSO Cannot be a junk value")
    public void junkPSO() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");

        loginPage = new CIDLoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions()
            .selectOverrideBundleCount()
            .setBundleCountOverride("jrigm");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions();

        assertThat(processSetupOptionsPage.isBundleCount(""), is(true));
    }
}