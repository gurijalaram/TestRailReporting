package com.evaluate.materialutilization;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.utils.enums.ProcessGroupEnum.FORGING;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.StockPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
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
    private SoftAssertions softAssertions = new SoftAssertions();

    public MaterialStockTests() {
        super();
    }

    @Test
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

        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Name")).isEqualTo("F-0005");
        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code")).isEqualTo("1.1");

        materialUtilizationPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial("FN-0205")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Name")).isEqualTo("FN-0205");
        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code")).isEqualTo("2.1");

        softAssertions.assertAll();
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
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Stock Form")).isEqualTo("ROUND_BAR");
        softAssertions.assertThat(stockPage.getStockInfo("Virtual Stock")).isEqualTo("Yes");

        stockPage.closePanel()
            .selectProcessGroup(FORGING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Selected Stock")).isEqualTo("Round Bar:  3 in  OD.  20 ft lengths");
        softAssertions.assertThat(stockPage.getStockInfo("Virtual Stock")).isEqualTo("No");

        softAssertions.assertAll();
    }
}
