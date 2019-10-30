package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.SelectStockPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class MaterialStockTests extends TestBase {

    private LoginPage loginPage;
    private SelectStockPage selectStockPage;
    private StockPage stockPage;
    private MaterialPage materialPage;

    public MaterialStockTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"862", "871"})
    @Description("Validate material name is updated in material and util panel")
    public void materialSelectionTest() {
        loginPage = new LoginPage(driver);
        materialPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Powder Metal.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .expandPanel();
        assertThat(materialPage.getMaterialInfo("Name"), is(equalTo("F-0005")));
        assertThat(materialPage.getMaterialInfo("Cut Code"), is(equalTo("1.1")));

        new MaterialPage(driver).closeMaterialAndUtilizationPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .openMaterialComposition();

        assertThat(materialPage.getMaterialInfo("Name"), is(equalTo("FN-0205")));
        assertThat(materialPage.getMaterialInfo("Cut Code"), is(equalTo("2.1")));
    }

    @Test
    @TestRail(testCaseId = {"962", "965", "966", "967"})
    @Description("Set the stock selection of a Scenario whose CAD file has material PMI attached uploaded via CI Design")
    public void materialPMIStock() {
        loginPage = new LoginPage(driver);
        stockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic_matPMI.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .expandPanel()
            .goToStockTab();
        assertThat(new StockPage(driver).checkTableDetails("Auto"), is(true));

        stockPage.editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        new MaterialPage(driver).closeMaterialAndUtilizationPanel();
        new EvaluatePage(driver).costScenario()
            .openMaterialComposition()
            .goToStockTab();
        assertThat(new StockPage(driver).checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        assertThat(new StockPage(driver).checkTableDetails("Manual"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"968"})
    @Description("check that Stock Form is accurate and updates correctly")
    public void stockForm() {
        loginPage = new LoginPage(driver);
        stockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Square circle.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialComposition()
            .expandPanel()
            .goToStockTab();
        assertThat(new StockPage(driver).checkTableDetails("ROUND_BAR"), is(true));
        assertThat(new StockPage(driver).checkTableDetails("Virtual Stock Yes"), is(true));
        assertThat(new StockPage(driver).checkTableDetails("Unit Cost (USD / kg) ↵ "), is(true));

        new MaterialPage(driver).closeMaterialAndUtilizationPanel();
        new EvaluatePage(driver).selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab();
        assertThat(new StockPage(driver).checkTableDetails("SQUARE_BAR"), is(true));
        assertThat(new StockPage(driver).checkTableDetails("Virtual Stock No"), is(true));
        assertThat(new StockPage(driver).checkTableDetails("Unit Cost (USD / kg) ↵2.85↵"), is(true));
    }
}
