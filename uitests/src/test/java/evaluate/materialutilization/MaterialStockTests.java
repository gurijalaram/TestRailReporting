package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.EvaluatePanelToolbar;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class MaterialStockTests extends TestBase {

    private CIDLoginPage loginPage;
    private StockPage stockPage;
    private MaterialPage materialPage;
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

        resourceFile = new FileResourceUtil().getResourceFile("Powder Metal.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePanelToolbar = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialComposition()
            .expandPanel();

        materialPage = new MaterialPage(driver);
        assertThat(materialPage.getMaterialInfo("Name"), is(equalTo("F-0005")));
        assertThat(materialPage.getMaterialInfo("Cut Code"), is(equalTo("1.1")));

        evaluatePanelToolbar = new EvaluatePanelToolbar(driver);
        materialPage = evaluatePanelToolbar.closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .openMaterialComposition();

        assertThat(materialPage.getMaterialInfo("Name"), is(equalTo("FN-0205")));
        assertThat(materialPage.getMaterialInfo("Cut Code"), is(equalTo("2.1")));
    }

    @Test
    @Issue("AP-59839")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"962", "965", "966", "967", "974", "970"})
    @Description("Set the stock selection of a Scenario whose CAD file has material PMI attached uploaded via CI Design")
    public void materialPMIStock() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic_matPMI.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();
        assertThat(evaluatePage.getPartCost(), is(equalTo("18.56")));

        evaluatePage = new EvaluatePage(driver);
        stockPage = evaluatePage.openMaterialComposition()
            .goToStockTab();
        assertThat(stockPage.checkTableDetails("Auto"), is(true));
        assertThat(stockPage.checkTableDetails("6.91"), is(true));

        stockPage = new StockPage(driver);
        stockPage.editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();
        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));

        evaluatePanelToolbar = new EvaluatePanelToolbar(driver);
        evaluatePage = evaluatePanelToolbar.closePanel()
            .costScenario();
        assertThat(evaluatePage.getPartCost(), is(equalTo("19.06")));

        evaluatePage = new EvaluatePage(driver);
        stockPage = evaluatePage.openMaterialComposition()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        assertThat(stockPage.checkTableDetails("Manual"), is(true));
        assertThat(stockPage.checkTableDetails("6.91"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"968", "969", "876"})
    @Description("check that Stock Form is accurate and updates correctly")
    public void stockForm() {

        resourceFile = new FileResourceUtil().getResourceFile("Square circle.CATPart");

        loginPage = new CIDLoginPage(driver);
        stockPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("ROUND_BAR"), is(true));
        assertThat(stockPage.checkTableDetails("Virtual Stock Yes"), is(true));

        stockPage.closePanel()
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("SQUARE_BAR"), is(true));
        assertThat(stockPage.checkTableDetails("Virtual Stock No"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3839","869"})
    @Description("validate the user can collapse and expand material properties")
    public void materialProperties() {

        resourceFile = new FileResourceUtil().getResourceFile("MultiUpload.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        new EvaluatePage(driver).openMaterialComposition();

        materialUtilizationPage = new MaterialUtilizationPage(driver)
            .toggleMaterialPropertiesPanel()
            .toggleUtilizationPanel();

        assertThat(materialUtilizationPage.utilizationPanelExpanded(), is("collapsed"));
        assertThat(materialUtilizationPage.materialPanelExpanded(), is("collapsed"));
    }
}
