package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.EvaluatePanelToolbar;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class MaterialStockTests extends TestBase {

    private CidLoginPage loginPage;
    private StockPage stockPage;
    private MaterialUtilizationPage materialUtilizationPage;
    private EvaluatePage evaluatePage;
    private EvaluatePanelToolbar evaluatePanelToolbar;

    private File resourceFile;

    public MaterialStockTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"862", "871"})
    @Description("Validate material name is updated in material and util panel")
    public void materialSelectionTest() {

        resourceFile = FileResourceUtil.getResourceAsFile("Powder Metal.stp");

        loginPage = new CidLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getMaterialInfo("Name"), is(equalTo("F-0005")));
        assertThat(materialUtilizationPage.getMaterialInfo("Cut Code"), is(equalTo("1.1")));

        materialUtilizationPage.closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getMaterialInfo("Name"), is(equalTo("FN-0205")));
        assertThat(materialUtilizationPage.getMaterialInfo("Cut Code"), is(equalTo("2.1")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"962", "965", "966", "967", "974", "970", "298"})
    @Description("Set the stock selection of a Scenario whose CAD file has material PMI attached uploaded via CI Design")
    public void materialPMIStock() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic_matPMI.prt.1");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();
        assertThat(evaluatePage.getPartCost(), is(closeTo(20.44, 1)));

        stockPage = evaluatePage.openMaterialUtilization()
            .goToStockTab();
        assertThat(stockPage.checkTableDetails("Auto"), is(true));
        assertThat(stockPage.checkTableDetails("7.65"), is(true));

        stockPage.editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();
        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));

        evaluatePage = stockPage.closePanel()
            .costScenario();
        assertThat(evaluatePage.getPartCost(), is(closeTo(20.99, 1)));

        stockPage = evaluatePage.openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        assertThat(stockPage.checkTableDetails("Manual"), is(true));
        assertThat(stockPage.checkTableDetails("7.65"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"968", "969", "876"})
    @Description("check that Stock Form is accurate and updates correctly")
    public void stockForm() {

        resourceFile = FileResourceUtil.getResourceAsFile("Square circle.CATPart");

        loginPage = new CidLoginPage(driver);
        stockPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("ROUND_BAR"), is(true));
        assertThat(stockPage.checkTableDetails("Virtual Stock Yes"), is(true));

        stockPage.closePanel()
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("3 in OD. 20 ft lengths"), is(true));
        assertThat(stockPage.checkTableDetails("Virtual Stock No"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3839", "869"})
    @Description("validate the user can collapse and expand material properties")
    public void materialProperties() {

        resourceFile = FileResourceUtil.getResourceAsFile("MultiUpload.stp");

        loginPage = new CidLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialUtilization()
            .toggleMaterialPropertiesPanel()
            .toggleUtilizationPanel();

        assertThat(materialUtilizationPage.utilizationPanelExpanded(), is("collapsed"));
        assertThat(materialUtilizationPage.materialPanelExpanded(), is("collapsed"));
    }
}
