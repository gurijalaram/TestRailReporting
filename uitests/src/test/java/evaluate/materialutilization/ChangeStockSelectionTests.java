package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.SelectStockPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.EvaluatePanelToolbar;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ChangeStockSelectionTests extends TestBase {

    private CIDLoginPage loginPage;
    private SelectStockPage selectStockPage;
    private StockPage stockPage;
    private EvaluatePanelToolbar evaluatePanelToolbar;

    private File resourceFile;

    public ChangeStockSelectionTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"960", "1617", "1618", "1619", "873"})
    @Description("Test making changes to the Material Stock in Sheet Metal, the change is respected and the scenario can be re-cost")
    public void changeStockSelectionTestSheetMetal() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        stockPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab()
            .editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));

        stockPage.closePanel()
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"983"})
    @Description("Test inappropriate stock cannot be selected")
    public void inappropriateStockSelectionTest() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        selectStockPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab()
            .editStock();

        assertThat(selectStockPage.getStockStatus("3.80  mm x 1219 mm x 3048 mm"), is(containsString("muted")));
    }
}