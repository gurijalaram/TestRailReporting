package com.evaluate.materialutilization;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.utils.enums.ProcessGroupEnum.FORGING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.StockPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class MaterialStockTests extends TestBase {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private StockPage stockPage;
    private MaterialUtilizationPage materialUtilizationPage;
    private File resourceFile;

    public MaterialStockTests() {
        super();
    }

    @Test
    @Issue("MIC-3085")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5115"})
    @Description("Validate material name is updated in material and util panel")
    public void materialSelectionTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "Powder Metal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("F-0005")));
        assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code"), is(equalTo("1.1")));

        materialUtilizationPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial("FN-0205")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("FN-0205")));
        assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code"), is(equalTo("2.1")));
    }

    /*@Test
    @TestRail(testCaseId = {"5148", "5151", "5152", "5153", "5156", "5160"})
    @Description("Set the stock selection of a Scenario whose CAD file has material PMI attached uploaded via CI Design")
    public void materialPMIStock() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic_matPMI";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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

        String componentName = "Square circle";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        stockPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .goToStockTab();

        assertThat(stockPage.getStockInfo("Stock Form"), is(equalTo("ROUND_BAR")));
        assertThat(stockPage.getStockInfo("Virtual Stock"), is(equalTo("Yes")));

        stockPage.closePanel()
            .selectProcessGroup(FORGING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .goToStockTab();

        assertThat(stockPage.getStockInfo("Selected Stock"), is(equalTo("Round Bar:  3 in  OD.  20 ft lengths")));
        assertThat(stockPage.getStockInfo("Virtual Stock"), is(equalTo("No")));
    }
}
