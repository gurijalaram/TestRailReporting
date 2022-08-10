package com.partsandassembliesdetails;



import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisCostDetailsEnum;
import com.utils.CisInsightsFieldsEnum;
import com.utils.CisScenarioResultsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;


public class PartsAndAssembliesDetailsTest extends TestBase {

    public PartsAndAssembliesDetailsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private File resourceFile;
    UserCredentials currentUser;

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

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderText()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerToolBarDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioResultsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInsightsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartsAndAssembliesLinkDisplayed()).isEqualTo(true);

        partsAndAssembliesPage = partsAndAssembliesDetailsPage.clicksPartsAndAssembliesLink();

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

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCostCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.TOTAL_COST.getFieldName())).contains(CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName(),CisScenarioResultsEnum.PIECE_PART_COST.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioInputsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.SCENARIO_INPUTS.getFieldName())).contains(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName(),
                CisScenarioResultsEnum.PROCESS_GROUP.getFieldName(),
                CisScenarioResultsEnum.ANNUAL_VOLUME.getFieldName(),
                CisScenarioResultsEnum.BATCH_SIZE.getFieldName(),
                CisScenarioResultsEnum.MATERIAL.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.MATERIAL.getFieldName())).contains(CisScenarioResultsEnum.FINISH_MASS.getFieldName(),
                CisScenarioResultsEnum.MATERIAL_COST.getFieldName(),
                CisScenarioResultsEnum.MATERIAL.getFieldName(),
                CisScenarioResultsEnum.MATERIAL_OVERHEAD.getFieldName(),
                CisScenarioResultsEnum.MATERIAL_UNIT_COST.getFieldName(),
                CisScenarioResultsEnum.ROUGH_MASS.getFieldName(),
                CisScenarioResultsEnum.UTILIZATION.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isManufacturingCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.MANUFACTURING.getFieldName())).contains(CisScenarioResultsEnum.CYCLE_TIME.getFieldName(),
                CisScenarioResultsEnum.DIRECT_OVERHEAD_COST.getFieldName(),
                CisScenarioResultsEnum.LABOR_COST.getFieldName(),
                CisScenarioResultsEnum.OTHER_DIRECT_COSTS.getFieldName(),
                CisScenarioResultsEnum.INDIRECT_OVERHEAD_COST.getFieldName(),
                CisScenarioResultsEnum.ROUTING.getFieldName(),
                CisScenarioResultsEnum.AMORTIZED_BATCH_SETUP_COST.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAdditionalCostCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.ADDITIONAL_COST.getFieldName())).contains(CisScenarioResultsEnum.LOGISTICS.getFieldName(),
                CisScenarioResultsEnum.MARGIN.getFieldName(),
                CisScenarioResultsEnum.SG_AND_A.getFieldName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCapitalExpensesCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName())).contains(CisScenarioResultsEnum.FIXTURE.getFieldName(),
                CisScenarioResultsEnum.HARD_TOOLING.getFieldName(),CisScenarioResultsEnum.PROGRAMMING.getFieldName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12910","12914"})
    @Description("Verify insights section and details for a costed scenario")
    public void testInsightsSection() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderTitleOnInsights()).isEqualTo("Insights");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessRoutingMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDfmRiskMenuIconDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12912","12913","12915","12916","13179"})
    @Description("Verify part nesting section on Insights")
    public void testPartNestingSection() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickPartNestingIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getPartNestingTitle()).isEqualTo("Part Nesting");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingGraphControllerDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingDetailsSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.DETAILS.getInsightsFields())).contains(CisInsightsFieldsEnum.UTILIZATION_INFO.getInsightsFields(),CisInsightsFieldsEnum.SELECTED_SHEET.getInsightsFields(),CisInsightsFieldsEnum.BLANK_SIZE.getInsightsFields(),
                CisInsightsFieldsEnum.PARTS_PER_SHEET.getInsightsFields(),CisInsightsFieldsEnum.CONFIGURATION.getInsightsFields(),CisInsightsFieldsEnum.UTILIZATION_MODE.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getPartNestingState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapsePartNestingSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getPartNestingState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13043","13044","13045","13046"})
    @Description("Verify material properties section on Insights")
    public void testMaterialPropertiesSection() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickMaterialPropertiesIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesTitle()).isEqualTo(CisInsightsFieldsEnum.MATERIAL_PROPERTIES.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.MATERIAL_PROPERTIES.getInsightsFields())).contains(CisInsightsFieldsEnum.CUT_COST.getInsightsFields(),CisInsightsFieldsEnum.USA_NAME.getInsightsFields(),CisInsightsFieldsEnum.DIN_NAME.getInsightsFields(),CisInsightsFieldsEnum.EN_NAME.getInsightsFields(),
                CisInsightsFieldsEnum.UNIT_COST.getInsightsFields(),CisInsightsFieldsEnum.DENSITY.getInsightsFields(),CisInsightsFieldsEnum.YOUNG_MODULE.getInsightsFields(),CisInsightsFieldsEnum.STRAIN_HARDENING_COEFFICIENT.getInsightsFields(),CisInsightsFieldsEnum.STRAIN_HARDENING_EXPONENT.getInsightsFields(),
                CisInsightsFieldsEnum.LANKFORD_PARAMETER.getInsightsFields(),CisInsightsFieldsEnum.MILLING_SPEED.getInsightsFields(),CisInsightsFieldsEnum.HARDNESS.getInsightsFields(),CisInsightsFieldsEnum.HARDNESS_SYSTEM.getInsightsFields(),CisInsightsFieldsEnum.POSSION_RATIO.getInsightsFields(),CisInsightsFieldsEnum.SHER_STRENGTH.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapseMaterialPropertiesSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13047","13048","13049","13050"})
    @Description("Verify material stock section on Insights")
    public void testMaterialStockSection() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickMaterialStockIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockTitle()).isEqualTo("Material Stock");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.BASIC_INFORMATION.getInsightsFields())).contains(CisInsightsFieldsEnum.SELECTED_STOCK.getInsightsFields(),CisInsightsFieldsEnum.SELECTED_METHOD.getInsightsFields(),CisInsightsFieldsEnum.STOCK_FORM.getInsightsFields(),
                CisInsightsFieldsEnum.VIRTUAL_STOCK.getInsightsFields(),CisInsightsFieldsEnum.UNIT_COST.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.DIMENSIONS.getInsightsFields())).contains(CisInsightsFieldsEnum.PART.getInsightsFields(),CisInsightsFieldsEnum.STOCK.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapseMaterialStockSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13180", "13182","13197"})
    @Description("Verify insights for a non-costed scenario")
    public void testInsightsSectionForUnCostedScenario() {
        String componentName = "bluesky-latch-cover";
        String scenarioName = "bluesky-latch-AutomationDetails";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockMenuIconDisplayed()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12485","12486","12487","12489","12490","12491","12492","12495"})
    @Description("Verify the creation of new scenario results cards")
    public void testCreateNewScenarioResultsCards() {
        String componentName = "ChampferOut";
        String scenarioName = "ChampferOut_AutomationDetails";
        String cardName = "Process Cost Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreateNewCardOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickToOpenModal()
                .closeModal();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreateCardModalDisplayed()).isEqualTo(false);

        partsAndAssembliesDetailsPage.clickToOpenModal();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getModalTitle()).isEqualTo("Card Settings");

        partsAndAssembliesDetailsPage.clickToOpenDropDown()
                .selectAnOption("Process Group");

        partsAndAssembliesDetailsPage.clickToOpenDropDown()
                .selectAnOption("Identity")
                .clickToOpenDropDown()
                .selectAnOption("Digital Factory");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSelectedFieldName()).contains("Process Group");

        partsAndAssembliesDetailsPage.clickToRemoveSelectedField();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSaveButtonStatus()).isEqualTo(false);

        partsAndAssembliesDetailsPage.enterCardName(cardName)
                .clickSaveButton();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedCardDisplayed(cardName)).isEqualTo(true);

        partsAndAssembliesDetailsPage.deleteScenarioResultsCard(cardName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12499","12500","12502","12503","12507","12509","12510","13088","13089","13090"})
    @Description("Verify scenario results cards can be edited")
    public void testEditScenarioResultsCards() {
        String componentName = "ChampferOut";
        String cardName = "Process Cost Card";
        String editedCardName = "Process Analysis Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickToOpenModal()
                .clickToOpenDropDown()
                .selectAnOption("Process Group")
                .clickToOpenDropDown()
                .selectAnOption("Identity")
                .clickToOpenDropDown()
                .selectAnOption("Digital Factory")
                .enterCardName(cardName)
                .clickSaveButton()
                .clickMoreOptions(cardName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isEditCardOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickEditCardOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCardName()).isEqualTo(cardName);

        partsAndAssembliesDetailsPage.clearCardName()
                .enterCardName(editedCardName)
                .removeSelectedField("Process Group")
                .clickToOpenDropDown()
                .selectAnOption("Annual Cost");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getNewSelectedFieldName()).contains("Annual Cost");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSaveButtonStatus()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickSaveButton();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedCardDisplayed(editedCardName)).isEqualTo(true);

        partsAndAssembliesDetailsPage.deleteScenarioResultsCard(editedCardName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12523","12524","12525","12527"})
    @Description("Verify scenario results cards can be removed")
    public void testRemoveScenarioResultsCards() {
        String componentName = "ChampferOut";
        String cardName = "Process Cost Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickToOpenModal()
                .clickToOpenDropDown()
                .selectAnOption("Process Group")
                .clickToOpenDropDown()
                .selectAnOption("Identity")
                .clickToOpenDropDown()
                .selectAnOption("Digital Factory")
                .enterCardName(cardName)
                .clickSaveButton()
                .clickMoreOptions(cardName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickRemoveCardOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteConfirmationModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDeleteModalTitle()).contains("Delete Card?");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDeleteModalContent()).contains("Once a card has been deleted, it cannot be undone.");

        partsAndAssembliesDetailsPage.clickDeleteButton();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedCardDisplayed(cardName)).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInsightsCardsDeleteOptionDisplayed("Design Guidance")).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13283","13144","13145","13146","13284","13165","13151","13162"})
    @Description("Verify process routing cycle time details card")
    public void testProcessRoutingCycleTimeDetailsCard() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(componentName,scenarioName)
                .clickProcessRouting();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessRoutingMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessRoutingCardDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectProcess("Cycle Time");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessRoutingCardTitle()).contains("Process Routing");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessDetailsSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCycleTimeGraphDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalSectionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickCycleTimeChart();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessRoutingDetails()).contains(CisCostDetailsEnum.PROCESS_GROUP_NAME.getProcessRoutingName(), CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName(),
                CisCostDetailsEnum.MACHINE_NAME.getProcessRoutingName(), CisCostDetailsEnum.CYCLE_TIME.getProcessRoutingName(),
                CisCostDetailsEnum.FULLY_BURDENED_COST.getProcessRoutingName(), CisCostDetailsEnum.PIECE_PART_COST.getProcessRoutingName(),
                CisCostDetailsEnum.TOTAL_CAPITAL_INVESTMENT.getProcessRoutingName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Group Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Machine Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Cycle Time")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Fully Burdened Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Piece Part Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Capital Investment")).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13215","13216","13217","13299"})
    @Description("Verify fully burdened cost details card")
    public void testProcessRoutingFullyBurdenedCostDetailsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickProcessRouting()
                .selectProcess("Fully Burdened Cost");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessDetailsSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isFullyBurdenedGraphDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalSectionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickFullyBurdenedCostChart();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessRoutingDetails()).contains(CisCostDetailsEnum.PROCESS_GROUP_NAME.getProcessRoutingName(), CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName(),
                CisCostDetailsEnum.MACHINE_NAME.getProcessRoutingName(), CisCostDetailsEnum.CYCLE_TIME.getProcessRoutingName(),
                CisCostDetailsEnum.FULLY_BURDENED_COST.getProcessRoutingName(), CisCostDetailsEnum.PIECE_PART_COST.getProcessRoutingName(),
                CisCostDetailsEnum.TOTAL_CAPITAL_INVESTMENT.getProcessRoutingName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Group Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Machine Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Cycle Time")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Fully Burdened Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Piece Part Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Capital Investment")).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13163","13164","13296","13166"})
    @Description("Verify piece part cost details card")
    public void testProcessRoutingPiecePartCostDetailsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
                .clickProcessRouting()
                .selectProcess("Piece Part Cost");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessDetailsSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPiecePartCostGraphDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalSectionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickPiecePartCostCostChart();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessRoutingDetails()).contains(CisCostDetailsEnum.PROCESS_GROUP_NAME.getProcessRoutingName(), CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName(),
                CisCostDetailsEnum.MACHINE_NAME.getProcessRoutingName(), CisCostDetailsEnum.CYCLE_TIME.getProcessRoutingName(),
                CisCostDetailsEnum.FULLY_BURDENED_COST.getProcessRoutingName(), CisCostDetailsEnum.PIECE_PART_COST.getProcessRoutingName(),
                CisCostDetailsEnum.TOTAL_CAPITAL_INVESTMENT.getProcessRoutingName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Group Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Machine Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Cycle Time")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Fully Burdened Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Piece Part Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Capital Investment")).isNotEmpty();

        softAssertions.assertAll();
    }
}


