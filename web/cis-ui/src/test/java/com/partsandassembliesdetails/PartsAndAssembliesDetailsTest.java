package com.partsandassembliesdetails;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisScenarioResultsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


public class PartsAndAssembliesDetailsTest extends TestBase {

    public PartsAndAssembliesDetailsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;


    @Test
    @TestRail(testCaseId = {"12396","12458","12460","12461","12254","12459"})
    @Description("Verify 3D viewer and column cards on parts and assemblies details page")
    public void testPartsAndAssembliesDetailPageHeaderTitle() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnScenarioName(scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderText()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerToolBarDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioResultsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInsightsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentsCardDisplayed()).isEqualTo(true);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(testCaseId = {"13067","13068","13069","13070","13071","13072"})
    @Description("Verify Scenario Results Default Cards on parts and assemblies details page")
    public void testScenarioResultsDefaultCards() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);

        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnScenarioName(scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCostCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.TOTAL_COST.getFieldName())).contains(CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName(),CisScenarioResultsEnum.PIECE_PART_COST.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioInputsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.SCENARIO_INPUTS.getFieldName())).contains(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName(),CisScenarioResultsEnum.PROCESS_GROUP.getFieldName(),CisScenarioResultsEnum.ANNUAL_VOLUME.getFieldName(),CisScenarioResultsEnum.BATCH_SIZE.getFieldName(),CisScenarioResultsEnum.MATERIAL.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.MATERIAL.getFieldName())).contains(CisScenarioResultsEnum.FINISH_MASS.getFieldName(),CisScenarioResultsEnum.MATERIAL_COST.getFieldName(),CisScenarioResultsEnum.MATERIAL.getFieldName(),CisScenarioResultsEnum.MATERIAL_OVERHEAD.getFieldName(),CisScenarioResultsEnum.MATERIAL_UNIT_COST.getFieldName(),CisScenarioResultsEnum.ROUGH_MASS.getFieldName(),CisScenarioResultsEnum.UTILIZATION.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isManufacturingCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.MANUFACTURING.getFieldName())).contains(CisScenarioResultsEnum.CYCLE_TIME.getFieldName(),CisScenarioResultsEnum.DIRECT_OVERHEAD_COST.getFieldName(),CisScenarioResultsEnum.LABOR_COST.getFieldName(),CisScenarioResultsEnum.OTHER_DIRECT_COSTS.getFieldName(),CisScenarioResultsEnum.INDIRECT_OVERHEAD_COST.getFieldName(),CisScenarioResultsEnum.ROUTING.getFieldName(),CisScenarioResultsEnum.AMORTIZED_BATCH_SETUP_COST.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAdditionalCostCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.ADDITIONAL_COST.getFieldName())).contains(CisScenarioResultsEnum.LOGISTICS.getFieldName(),CisScenarioResultsEnum.MARGIN.getFieldName(),CisScenarioResultsEnum.SG_AND_A.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCapitalExpensesCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName())).contains(CisScenarioResultsEnum.FIXTURE.getFieldName(),CisScenarioResultsEnum.HARD_TOOLING.getFieldName(),CisScenarioResultsEnum.PROGRAMMING.getFieldName());

        softAssertions.assertAll();
    }

}


