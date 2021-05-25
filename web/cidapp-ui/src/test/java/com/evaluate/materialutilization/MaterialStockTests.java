package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.StockPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class MaterialStockTests extends TestBase {

    private CidAppLoginPage loginPage;
    private StockPage stockPage;
    private MaterialUtilizationPage materialUtilizationPage;

    private File resourceFile;

    public MaterialStockTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5115"})
    @Description("Validate material name is updated in material and util panel")
    public void materialSelectionTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Powder Metal.stp");

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .inputDigitalFactory(DigitalFactoryEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("F-0005")));
        assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code"), is(equalTo("1.1")));

        materialUtilizationPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial("FN-0205")
            .submit()
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("FN-0205")));
        assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code"), is(equalTo("2.1")));
    }

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5148", "5151", "5152", "5153", "5156", "5160"})
    @Description("Set the stock selection of a Scenario whose CAD file has material PMI attached uploaded via CI Design")
    public void materialPMIStock() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic_matPMI.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .inputVpe(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostResults("Piece Part Cost"), is(closeTo(20.44, 1)));

        evaluatePage.openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.getStockInfo("Selection Method"), is(equalTo("Auto")));
        assertThat(stockPage.getStockInfo("Unit Cost (USD / kg)"), is(equalTo("7.65")));

        stockPage.editStock()
            .selectStock("4.00  mm x 1500 mm x 3000 mm")
            .apply();
        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));

        evaluatePage = stockPage.closePanel()
            .costScenario();

        assertThat(evaluatePage.getCostResults("Piece Part Cost"), is(closeTo(20.99, 1)));

        stockPage = evaluatePage.openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.checkTableDetails("4.00 mm x 1500 mm x 3000 mm"), is(true));
        assertThat(stockPage.checkTableDetails("Manual"), is(true));
        assertThat(stockPage.checkTableDetails("7.65"), is(true));
    }*/

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5154", "5155", "5156"})
    @Description("check that Stock Form is accurate and updates correctly")
    public void stockForm() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Square circle.CATPart");

        loginPage = new CidAppLoginPage(driver);
        stockPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .inputDigitalFactory(DigitalFactoryEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.getStockInfo("Stock Form"), is(equalTo("ROUND_BAR")));
        assertThat(stockPage.getStockInfo("Virtual Stock"), is(equalTo("Yes")));

        stockPage.closePanel()
            .inputProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToStockTab();

        assertThat(stockPage.getStockInfo("Stock Form"), is(equalTo("3 in OD. 20 ft lengths")));
        assertThat(stockPage.getStockInfo("Virtual Stock"), is(equalTo("No")));
    }
}
