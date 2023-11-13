package com.apriori.cid.ui.tests.evaluate.materialutilization;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.models.dto.ComponentDTORequest;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ChangeMaterialSelectionTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private MaterialUtilizationPage materialUtilizationPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @TestRail(id = {6186, 5898})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.CASTING_SAND);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("270")
            .selectMaterial(MaterialNameEnum.BRASS_YELLOW_270.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.BRASS_YELLOW_270.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6187})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("C28000")
            .selectMaterial(MaterialNameEnum.COPPER_UNS_C28000.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.COPPER_UNS_C28000.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6188})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ABS.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("PET")
            .selectMaterial(MaterialNameEnum.PET_30_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.PET_30_GLASS.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6189})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("625")
            .selectMaterial(MaterialNameEnum.INCONEL_625.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.INCONEL_625.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6190, 5420})
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectDigitalFactory(APRIORI_USA)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .openScenario(component.getComponentName(), component.getScenarioName());

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6191})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialSelectorTable()
            .search("PEEK")
            .selectMaterial(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName()), is(true));
    }

    @Test
    @TestRail(id = {6192, 5896})
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialSelectorTable()
            .search("625")
            .selectMaterial(MaterialNameEnum.INCONEL_625.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo(MaterialNameEnum.INCONEL_625.getMaterialName())));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6193, 5420, 5910, 6303})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component.getComponentName(), component.getScenarioName());

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1095.getMaterialName()), is(true));
    }

    @Test
    @TestRail(id = {6194, 5911})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterialMode("Digital Factory Default [Steel, Hot Worked, AISI 1010]")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName()), is(true));
    }

    @Test
    @TestRail(id = {6195, 5921})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {
        component = new ComponentDTORequest().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .openMaterialSelectorTable()
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())).isTrue();

        evaluatePage.openMaterialSelectorTable()
            .search("316")
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .cancel(EvaluatePage.class)
            .costScenario()
            .confirmCost("Yes");

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())).isTrue();

        softAssertions.assertAll();
    }
}