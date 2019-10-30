package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
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

public class MaterialStockTests extends TestBase {

    private LoginPage loginPage;
    private SelectStockPage selectStockPage;
    private StockPage stockPage;
    private MaterialPage materialPage;

    public MaterialStockTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"862"})
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
        assertThat(materialPage.getMaterialInfo("Cut Code"), is(equalTo("2.1")));

        new MaterialPage(driver).closeMaterialAndUtilizationPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .openMaterialComposition();

        assertThat(materialPage.getMaterialInfo("Name"), is(equalTo("FN-0205")));
    }

}
