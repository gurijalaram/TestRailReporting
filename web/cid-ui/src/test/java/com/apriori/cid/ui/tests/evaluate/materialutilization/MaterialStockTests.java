package com.apriori.cid.ui.tests.evaluate.materialutilization;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.enums.ProcessGroupEnum.FORGING;
import static com.apriori.shared.util.enums.ProcessGroupEnum.POWDER_METAL;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.StockPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class MaterialStockTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private StockPage stockPage;
    private MaterialUtilizationPage materialUtilizationPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public MaterialStockTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5115})
    @Description("Validate material name is updated in material and util panel")
    public void materialSelectionTest() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Name")).isEqualTo("F-0005");
        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code")).isEqualTo("1.1");

        materialUtilizationPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_FN025.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Name")).isEqualTo(MaterialNameEnum.STEEL_FN025.getMaterialName());
        softAssertions.assertThat(materialUtilizationPage.getUtilizationInfo("Cut Code")).isEqualTo("2.1");

        softAssertions.assertAll();
    }

    /*@Test
    @TestRail(id = {5148,5151", "5152", "5153", "5156", "5160"})
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
    @Tag(SMOKE)
    @TestRail(id = {5154, 5155, 5156})
    @Description("check that Stock Form is accurate and updates correctly")
    public void stockForm() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        stockPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
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
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Selected Stock")).isEqualTo("Round Bar:  3 in  OD.  20 ft lengths");
        softAssertions.assertThat(stockPage.getStockInfo("Virtual Stock")).isEqualTo("No");

        softAssertions.assertAll();
    }
}
