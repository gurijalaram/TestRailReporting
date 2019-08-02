package test.java.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.materialutilization.stock.StockPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.properties.reader.FileResourceReader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class ChangeStockSelectionTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public ChangeStockSelectionTests() {
        super();
    }

    @Test
    @Description("Test making changes to the Material Stock, the change is respected and the scenario can be re-cost")
    @Severity(SeverityLevel.NORMAL)
    public void changeStockSelectionTest() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ChangeScenarioStockSelection", new FileResourceReader().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openMaterialComposition()
            .goToStockTab()
            .editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();

        assertThat(new StockPage(driver).checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        new EvaluatePage(driver).costScenario((CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()));
        assertThat(new StockPage(driver).checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
    }
}