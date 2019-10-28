package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.SelectStockPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class ChangeStockSelectionTests extends TestBase {

    private LoginPage loginPage;
    private SelectStockPage selectStockPage;
    private StockPage stockPage;

    public ChangeStockSelectionTests() {
        super();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"960", "1617", "1618", "1619"})
    @Description("Test making changes to the Material Stock, the change is respected and the scenario can be re-cost")
    public void changeStockSelectionTest() {
        loginPage = new LoginPage(driver);
        stockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab()
            .editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        new EvaluatePage(driver).costScenario();
        assertThat(new StockPage(driver).checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"983"})
    @Description("Test inappropriate stock cannot be selected")
    public void inappropriateStockSelectionTest() {
        loginPage = new LoginPage(driver);
        selectStockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab()
            .editStock();

        assertThat(selectStockPage.getStockStatus("3.80  mm x 1219 mm x 3048 mm"), is(containsString("muted")));
    }
}