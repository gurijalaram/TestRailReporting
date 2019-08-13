package test.java.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.materialutilization.stock.SelectStockPage;
import main.java.pages.evaluate.materialutilization.stock.StockPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class ChangeStockSelectionTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private SelectStockPage selectStockPage;
    private StockPage stockPage;

    public ChangeStockSelectionTests() {
        super();
    }

    @Test
    @Description("Test making changes to the Material Stock, the change is respected and the scenario can be re-cost")
    @Severity(SeverityLevel.NORMAL)
    public void changeStockSelectionTest() {
        loginPage = new LoginPage(driver);
        stockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
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
    @Description("Test inappropriate stock cannot be selected")
    @Severity(SeverityLevel.NORMAL)
    public void inappropriateStockSelectionTest() {
        loginPage = new LoginPage(driver);
        selectStockPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToStockTab()
            .editStock();

        assertThat(selectStockPage.getStockStatus("3.80  mm x 1219 mm x 3048 mm"), is(containsString("muted")));
    }
}