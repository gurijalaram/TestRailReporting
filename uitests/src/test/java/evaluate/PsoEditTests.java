package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class PsoEditTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ProcessSetupOptionsPage processSetupOptionsPage;
    private ProcessRoutingPage processRoutingPage;

    @Test
    @TestRail(testCaseId = {"761", "762", "763", "764"})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
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
        assertThat(processSetupOptionsPage.isNominalOverride("0.4"), is(true));
        assertThat(processSetupOptionsPage.isAddColorantSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.isMaterialRegrind("0.3"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"771", "772", "773"})
    @Description("Die Casting edit PSO")
    public void DieCastPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting-Die.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions()
            .selectOptimizeButton()
            .selectMoldMaterialDropdown("AISI P20")
            .selectPartToleranceDropdown("Low Tolerance +/-0.254 (+/-0.010\")");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("High Pressure Die Casting")
            .selectOptions();

        assertThat(processSetupOptionsPage.isOptimizeForMinimumCostSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.getMoldMaterial("AISI P20"), is(true));
        assertThat(processSetupOptionsPage.getSelectedPartTolerance("Low Tolerance +/-0.254 (+/-0.010\")"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"769", "770"})
    @Description("Sand Casting edit PSO")
    public void SandCastPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SandCast.x_t"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions()
            .selectOptimizeButton()
            .selectMoldMaterialDropdown("Plastic");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Vertical Automatic")
            .selectOptions();

        assertThat(processSetupOptionsPage.isOptimizeForMinimumCostSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.getMoldMaterial("Plastic"), is(true));
    }
    @Test
    @TestRail(testCaseId = {"768"})
    @Description("Machining - Validate the user can edit bundle sawing count")
    public void MachiningPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw")
            .selectOptions()
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
    public void PowderMetalPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Compaction Pressing")
            .selectOptions()
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
    public void SheetPlasticPSO() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("sheet_plastic.STEP"))
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
}
