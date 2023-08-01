package com.apriori.partsandassembliesdetails;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.CisColumnsEnum;
import com.utils.CisCostDetailsEnum;
import com.utils.CisDesignGuidanceDetailsEnum;
import com.utils.CisInsightsFieldsEnum;
import com.utils.CisScenarioResultsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PartsAndAssembliesDetailsTest extends TestBaseUI {

    private CisLoginPage loginPage;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private File resourceFile;
    private UserCredentials currentUser;
    private EvaluatePage evaluatePage;

    public PartsAndAssembliesDetailsTest() {
        super();
    }

    @Test
    @TestRail(id = {12254, 12396, 12459, 12460, 12461})
    @Description("Verify 3D viewer and column cards on parts and assemblies details page")
    public void testPartsAndAssembliesDetailPageHeaderTitle() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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
    @TestRail(id = {13067, 13068, 13069, 13070, 13071, 13072})
    @Description("Verify Scenario Results Default Cards on parts and assemblies details page")
    public void testScenarioResultsDefaultCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCostCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getScenarioResultCardFieldsName(CisScenarioResultsEnum.TOTAL_COST.getFieldName())).contains(CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName(), CisScenarioResultsEnum.PIECE_PART_COST.getFieldName());

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
            CisScenarioResultsEnum.HARD_TOOLING.getFieldName(), CisScenarioResultsEnum.PROGRAMMING.getFieldName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12910, 12914})
    @Description("Verify insights section and details for a costed scenario")
    public void testInsightsSection() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12912, 12913, 12915, 12916, 13179})
    @Description("Verify part nesting section on Insights")
    public void testPartNestingSection() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.DETAILS.getInsightsFields())).contains(CisInsightsFieldsEnum.UTILIZATION_INFO.getInsightsFields(),
            CisInsightsFieldsEnum.SELECTED_SHEET.getInsightsFields(), CisInsightsFieldsEnum.BLANK_SIZE.getInsightsFields(),
            CisInsightsFieldsEnum.PARTS_PER_SHEET.getInsightsFields(), CisInsightsFieldsEnum.CONFIGURATION.getInsightsFields(),
            CisInsightsFieldsEnum.UTILIZATION_MODE.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getPartNestingState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapsePartNestingSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getPartNestingState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13043, 13044, 13045, 13046})
    @Description("Verify material properties section on Insights")
    public void testMaterialPropertiesSection() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickMaterialPropertiesIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesTitle()).isEqualTo(CisInsightsFieldsEnum.MATERIAL_PROPERTIES.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.MATERIAL_PROPERTIES.getInsightsFields())).contains(CisInsightsFieldsEnum.CUT_COST.getInsightsFields(),
            CisInsightsFieldsEnum.USA_NAME.getInsightsFields(), CisInsightsFieldsEnum.DIN_NAME.getInsightsFields(), CisInsightsFieldsEnum.EN_NAME.getInsightsFields(),
            CisInsightsFieldsEnum.UNIT_COST.getInsightsFields(), CisInsightsFieldsEnum.DENSITY.getInsightsFields(), CisInsightsFieldsEnum.YOUNG_MODULE.getInsightsFields(),
            CisInsightsFieldsEnum.STRAIN_HARDENING_COEFFICIENT.getInsightsFields(), CisInsightsFieldsEnum.STRAIN_HARDENING_EXPONENT.getInsightsFields(),
            CisInsightsFieldsEnum.LANKFORD_PARAMETER.getInsightsFields(), CisInsightsFieldsEnum.MILLING_SPEED.getInsightsFields(), CisInsightsFieldsEnum.HARDNESS.getInsightsFields(),
            CisInsightsFieldsEnum.HARDNESS_SYSTEM.getInsightsFields(), CisInsightsFieldsEnum.POSSION_RATIO.getInsightsFields(), CisInsightsFieldsEnum.SHER_STRENGTH.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapseMaterialPropertiesSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialPropertiesState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13047, 13048, 13049, 13050})
    @Description("Verify material stock section on Insights")
    public void testMaterialStockSection() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickMaterialStockIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockTitle()).isEqualTo("Material Stock");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.BASIC_INFORMATION.getInsightsFields())).contains(CisInsightsFieldsEnum.SELECTED_STOCK.getInsightsFields(),
            CisInsightsFieldsEnum.SELECTED_METHOD.getInsightsFields(), CisInsightsFieldsEnum.STOCK_FORM.getInsightsFields(),
            CisInsightsFieldsEnum.VIRTUAL_STOCK.getInsightsFields(), CisInsightsFieldsEnum.UNIT_COST.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getItemsOfSections(CisInsightsFieldsEnum.DIMENSIONS.getInsightsFields())).contains(CisInsightsFieldsEnum.PART.getInsightsFields(),
            CisInsightsFieldsEnum.STOCK.getInsightsFields());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockState()).isEqualTo("caret-up");

        partsAndAssembliesDetailsPage.collapseMaterialStockSection();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getMaterialStockState()).isEqualTo("caret-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13180, 13182, 13197, 13924})
    @Description("Verify insights for a non-costed scenario")
    public void testInsightsSectionForUnCostedScenario() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "bluesky-latch-cover";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockMenuIconDisplayed()).isEqualTo(false);

        partsAndAssembliesDetailsPage.clickCostsOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getUnCostMessage()).contains("Cost the scenario to see the cost result summary.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12485, 12486, 12487, 12489, 12490, 12491, 12492, 12495})
    @Description("Verify the creation of new scenario results cards")
    public void testCreateNewScenarioResultsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        String cardName = "Process Cost Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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
            .selectAnOption("Process Group")
            .selectAnOption("Additional Amortized Investment")
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
    @TestRail(id = {12499, 12500, 12502, 12503, 12507, 12509, 12510, 13088, 13089, 13090})
    @Description("Verify scenario results cards can be edited")
    public void testEditScenarioResultsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        String cardName = "Process Cost Card";
        String editedCardName = "Process Analysis Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickToOpenModal()
            .clickToOpenDropDown()
            .selectAnOption("Process Group")
            .selectAnOption("Additional Amortized Investment")
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

        partsAndAssembliesDetailsPage.clickToOpenDropDown()
            .clickSaveButton();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedCardDisplayed(editedCardName)).isEqualTo(true);

        partsAndAssembliesDetailsPage.deleteScenarioResultsCard(editedCardName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12523, 12524, 12525, 12527})
    @Description("Verify scenario results cards can be removed")
    public void testRemoveScenarioResultsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        String cardName = "Process Cost Card";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickToOpenModal()
            .clickToOpenDropDown()
            .selectAnOption("Process Group")
            .selectAnOption("Additional Amortized Investment")
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
    @TestRail(id = {13283, 13144, 13145, 13146, 13284, 13165, 13151, 13162})
    @Description("Verify process routing cycle time details card")
    public void testProcessRoutingCycleTimeDetailsCard() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(componentName, scenarioName)
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
    @TestRail(id = {13215, 13216, 13217, 13299, 14542})
    @Description("Verify fully burdened cost details card")
    public void testProcessRoutingFullyBurdenedCostDetailsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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
    @TestRail(id = {13163, 13164, 13296, 13166, 14541})
    @Description("Verify piece part cost details card")
    public void testProcessRoutingPiecePartCostDetailsCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
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

    @Test
    @TestRail(id = {13243, 13244, 13245, 13248, 13485, 13488})
    @Description("Verify assembly tree view")
    public void testAssemblyTreeView() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(assemblyName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(assemblyName, scenarioName)
            .clickAssemblyTree();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeViewDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns());

        partsAndAssembliesDetailsPage.clickShowHideOption()
            .hideField("State");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).doesNotContain(CisColumnsEnum.STATE.getColumns());

        partsAndAssembliesDetailsPage.openAssembly("Pin", scenarioName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubComponentName()).isEqualTo("Pin");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13320, 13321, 13322, 13323, 13324, 13375, 13376, 13377})
    @Description("Verify design guidance issue details")
    public void testDesignGuidanceIssueDetails() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickDesignGuidance()
            .clickIssueCollapsibleIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getIssueCollapsibleIcons()).isEqualTo("caret-down");

        partsAndAssembliesDetailsPage.clickIssueCollapsibleIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getIssueCollapsibleIcons()).isEqualTo("caret-up");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDesignGuidanceCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isIssuesPanelDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDesignGuidanceDetails()).contains(CisDesignGuidanceDetailsEnum.HOLE_ISSUE.getDesignGuidanceDetailsName(), CisDesignGuidanceDetailsEnum.PROXIMITY_WARNING.getDesignGuidanceDetailsName(),
            CisDesignGuidanceDetailsEnum.BLANK_ISSUE.getDesignGuidanceDetailsName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getIssueDetails("Hole Issue")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getIssueDetails("Proximity Warning")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getIssueDetails("Blank Issue")).isNotEmpty();

        partsAndAssembliesDetailsPage.clickOnHoleIssue();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isGCDTableDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.geCheckBoxStatus()).contains("Mui-checked");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13584, 13585, 13586, 13588, 13589, 13590})
    @Description("Verify design guidance investigation section details")
    public void testDesignGuidanceInvestigationDetails() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickDesignGuidance()
            .clickInvestigationCollapsibleIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getInvestigationCollapsibleState()).isEqualTo("caret-down");

        partsAndAssembliesDetailsPage.clickInvestigationCollapsibleIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getInvestigationCollapsibleState()).isEqualTo("caret-up");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDesignGuidanceCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInvestigationPanelDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getInvestigationTopics()).contains(CisDesignGuidanceDetailsEnum.DISTINCT_SIZES_COUNT.getDesignGuidanceDetailsName(), CisDesignGuidanceDetailsEnum.MACHINING_SETUPS.getDesignGuidanceDetailsName(),
            CisDesignGuidanceDetailsEnum.MACHINED_GCD.getDesignGuidanceDetailsName());

        partsAndAssembliesDetailsPage.clickOnDistinctSizesCount();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isGCDTableDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.geCheckBoxStatus()).contains("Mui-checked");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13799, 13800, 13801, 13992, 13993, 13996, 13997})
    @Description("Verify cost details for part")
    public void testPartCostCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostsOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickCostsOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCostTitle()).isEqualTo("Cost");

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.FULLY_BURDENED_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isFullyBurdenedCostCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.PIECE_PART_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPiecePartCostCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.TOTAL_CAPITAL_INVESTMENT.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCapitalInvestmentCostGraphDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14033, 14034, 14035, 14036, 14037, 14038, 14039})
    @Description("Verify design guidance threads section details")
    public void testDesignGuidanceThreadsDetails() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickDesignGuidance()
            .clickThreadsCollapsibleIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getThreadsCollapsibleState()).isEqualTo("caret-down");

        partsAndAssembliesDetailsPage.clickThreadsCollapsibleIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getThreadsCollapsibleState()).isEqualTo("caret-up");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDesignGuidanceCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isThreadsPanelDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getThreadsItems()).contains(CisDesignGuidanceDetailsEnum.SIMPLE_HOLES.getDesignGuidanceDetailsName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getThreadsCount("Simple Holes")).isNotEmpty();

        partsAndAssembliesDetailsPage.clickOnSimpleHolesItem();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isGCDTableDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.geCheckBoxStatus()).contains("Mui-checked");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14040, 14041, 14042, 14043, 14044, 14045, 14046, 14047})
    @Description("Verify assembly cost details breakdowns")
    public void testAssemblyCostDetails() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(assemblyName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(assemblyName, scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyCostsOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickCostsOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCostTitle()).isEqualTo("Cost");

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.ASSEMBLY_PROCESS_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyProcessCostCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.COMPONENT_COST_FULLY_BURDENED.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isComponentCostFullyBurdenedCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.COMPONENT_COST_PIECE_PART.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isComponentCostPiecePartCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.TOTAL_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCostGraphDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13270, 13271, 13272, 13273, 13274, 13275, 13276})
    @Description("Verify share scenario functionalities")
    public void testShareScenarioFunction() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShareBtnDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnShare();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShareScenarioModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUsersDropDownDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInviteButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isSharedParticipantsDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectAUser("qa-automation-01@apriori.com")
            .selectAUser("qa-automation-02@apriori.com");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSelectedUserName()).contains("QA Automation Account 01");

        partsAndAssembliesDetailsPage.clickOnUserRemoveIcon()
            .clickOnInvite();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13307, 13308, 13309, 13310, 13314, 13315, 13316, 14180})
    @Description("Verify that discussions can be created by scenario results attributes")
    public void testCreateDiscussionByScenarioAttributes() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMessageIconDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickMessageIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentThreadModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttribute()).isEqualTo(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.clickCancel();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAbandonCommentModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAbandonCommentModalContent()).isEqualTo("if you abandon, what you've written will be lost.");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAbandonButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isKeepEditingButtonDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickKeepEditing()
            .addComment("New Comment for Digital Factory")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionAttribute()).isEqualTo(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("New Comment for Digital Factory");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14466, 14467, 14468, 14469, 14470, 14471, 14543, 14641, 14691})
    @Description("Verify that discussions can be created by comment section")
    public void testCreateDiscussionByCommentSection() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMessageIconDisplayedOnComments()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentThreadModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.clickOnAttribute();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttributeList()).contains(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName(), CisScenarioResultsEnum.PROCESS_GROUP.getFieldName(), CisScenarioResultsEnum.MATERIAL.getFieldName(),
            CisScenarioResultsEnum.MANUFACTURING.getFieldName(), CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName(),
            CisScenarioResultsEnum.PIECE_PART_COST.getFieldName(), CisScenarioResultsEnum.ANNUAL_VOLUME.getFieldName(),
            CisScenarioResultsEnum.BATCH_SIZE.getFieldName(), CisScenarioResultsEnum.FINISH_MASS.getFieldName());

        partsAndAssembliesDetailsPage.selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionAttribute()).isEqualTo(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("New Comment With Attribute");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatorAvatarDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection()
            .addComment("New Comment Without Attribute")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("New Comment Without Attribute");
    }

    @Test
    @TestRail(id = {14183, 14184, 14185, 14186, 14187, 14691})
    @Description("Verify that replies can be added to a selected comment thread")
    public void testReplyToACommentThread() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isReplyFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isReplyButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonOnDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.addComment("New Reply")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getReplyMessage()).contains("New Reply");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getRepliesCount()).contains("1 reply");

        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment("Second Discussion")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttributeDiscussionCount()).contains("2");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14189, 14190, 14697, 14698, 14699})
    @Description("Verify that user can resolve and unresolve a discussion")
    public void testResolveAndUnresolvedComment() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponentName(componentName)
            .clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isResolveOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnResolveIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getResolveStatus()).contains("resolved");

        partsAndAssembliesDetailsPage.clickOnUnResolveIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getUnResolveStatus()).contains("active");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14195, 14196, 14201, 14202, 14730, 14732})
    @Description("Verify that user can delete and undelete a discussion")
    public void testDeleteAndUndeleteDiscussion() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponentName(componentName)
            .clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion()
            .clickMoreOption();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteDiscussionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickDeleteDiscussion();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteDiscussionConfirmationModalDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickDeleteDiscussionButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUndoDeleteOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUndoDeleteDiscussionButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14191, 14192, 14193, 14194, 14628, 14689, 14690})
    @Description("Verify that assign and un-assign a discussion to a project participant ")
    public void testAssignUnAssignACommentThread() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion()
            .clickMoreOption();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssignToOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickAssignToOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isParticipantsListDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectAParticipant("QA Automation Account");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("You");

        partsAndAssembliesDetailsPage.shareScenario()
            .selectAUser("qa-automation-22@apriori.com")
            .clickOnInvite()
            .clickOnCreatedDiscussion()
            .clickMoreOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUnAssignOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUnAssignOption()
            .reassignDiscussion("QA Automation Account 22");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 22");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14700, 14701, 14702, 14760, 14761})
    @Description("Verify that user can delete and undelete a comment")
    public void testDeleteAndUndeleteAComment() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponentName(componentName)
            .clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion()
            .addComment("New Reply")
            .clickComment();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentMoreOptionMenuDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnCommentMoreOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteCommentOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnDeleteCommentOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUndoDeleteOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUnDeleteCommentOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeletedReplyDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14728, 14729, 14731, 14736, 14733, 14734})
    @Description("Verify that mention users in a comment and assign comment to a mention user ")
    public void testMentionUsersOnACommentThread() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponentName(componentName)
            .clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion()
            .addCommentWithMention("This is a new reply with a mention user @22");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMentionUserPickerDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectMentionUser("QA Automation Account");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAMentionUserTagDisplayed("QA Automation Account 22")).isEqualTo(true);

        partsAndAssembliesDetailsPage.addCommentWithMention("second mention user @23")
            .selectMentionUser("qa-automation-23@apriori.com")
            .clickChangeAssigneeOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssignToAMentionUserListDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectMentionUserToAssignDiscussion("qa-automation-23@apriori.com")
            .clickToAssign()
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 23");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14050, 14051, 14052, 14053})
    @Description("Verify remove participants functionalities")
    public void testRemoveParticipants() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName)
            .clickOnShare();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShareScenarioModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUsersDropDownDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInviteButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isSharedParticipantsDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectAUser("qa-automation-01@apriori.com")
            .selectAUser("qa-automation-02@apriori.com")
            .clickOnInvite();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveIconDisplayed()).isTrue();

        partsAndAssembliesDetailsPage.clickOnSharedUserRemoveIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveModalDisplayed()).isTrue();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getRemoveParticipantMessageText()).contains("Are you want to remove yourself from the workspace?");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveButtonDisplayed()).isTrue();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveCancelButtonDisplayed()).isTrue();

        partsAndAssembliesDetailsPage.clickOnSharedUserRemoveCancelButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShareScenarioModalDisplayed()).isTrue();

        partsAndAssembliesDetailsPage.clickOnSharedUserRemoveIcon()
            .clickOnSharedUserRemoveButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isRemoveIconDisplayed()).isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16676, 16678})
    @Description("Verify user can open the same component in CID app from details view")
    public void testOpenComponentInCID() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isOpenInCIDButtonDisplayed()).isEqualTo(true);

        evaluatePage = partsAndAssembliesDetailsPage.clickOnOpenComponent()
            .clickOnCid()
            .switchTab();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {17093, 17094, 17095, 17096, 17098, 17099})
    @Description("Verify discussions can create from process details card")
    public void testCreateDiscussionsFromProcessDetailsCard() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName)
            .clickOnComponent(componentName, scenarioName)
            .clickProcessRouting()
            .selectProcess("Cycle Time")
            .clickCycleTimeChart();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isNewMessageOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickProcessNameMessageIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentThreadModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttribute()).isEqualTo(CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.clickCancel();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAbandonCommentModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAbandonCommentModalContent()).isEqualTo("if you abandon, what you've written will be lost.");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAbandonButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isKeepEditingButtonDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickKeepEditing()
            .addComment("Process Card Data Field Comment")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionAttribute()).isEqualTo(CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("Process Card Data Field Comment");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16672, 16673, 16838, 16839, 16840})
    @Description("Verify user can add/remove process details fields")
    public void testEnableConfigurationsOfProcessDetailsCard() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(componentName, scenarioName)
            .clickProcessRouting()
            .selectProcess("Cycle Time")
            .clickCycleTimeChart();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMoreOptionsMenuDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isNewMessageOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnMoreOptionMenu()
            .clickEditProcessCardOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessCardModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getModalTitle()).isEqualTo("Card Settings");

        partsAndAssembliesDetailsPage.openAndCloseProcessDropDown()
            .selectAnOption("Fixture Cost")
            .selectAnOption("Total Machine Cost")
            .selectAnOption("Labor Time");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSelectedProcessFieldName()).contains("Fixture Cost");

        partsAndAssembliesDetailsPage.clickToRemoveProcessField()
            .openAndCloseProcessDropDown()
            .clickProcessModalSaveButton()
            .clickProcessRouting()
            .clickCycleTimeChart();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessRoutingDetails()).contains(CisCostDetailsEnum.PROCESS_GROUP_NAME.getProcessRoutingName(), CisCostDetailsEnum.PROCESS_NAME.getProcessRoutingName(),
            CisCostDetailsEnum.MACHINE_NAME.getProcessRoutingName(), CisCostDetailsEnum.CYCLE_TIME.getProcessRoutingName(),
            CisCostDetailsEnum.FULLY_BURDENED_COST.getProcessRoutingName(), CisCostDetailsEnum.PIECE_PART_COST.getProcessRoutingName(),
            CisCostDetailsEnum.TOTAL_CAPITAL_INVESTMENT.getProcessRoutingName(), CisCostDetailsEnum.TOTAL_MACHINE_COST.getProcessRoutingName(),
            CisCostDetailsEnum.LABOR_TIME.getProcessRoutingName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Group Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Process Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Machine Name")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Cycle Time")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Fully Burdened Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Piece Part Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Capital Investment")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Machine Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Labor Time")).isNotEmpty();

        partsAndAssembliesDetailsPage.resetToDefaultConfiguration();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16460, 16461, 16462, 16463})
    @Description("Verify non-applicable fields are hidden in scenario info cards")
    public void testShowHideNonApplicableFieldsInScenarioInfoCards() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        String cardName = "Liability Inputs";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponentName(componentName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreateNewCardOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickToOpenModal()
            .clickToOpenDropDown()
            .selectAnOption("Liability Insurance Cost")
            .selectAnOption("Liability Insurance Factor")
            .selectAnOption("Logistics")
            .selectAnOption("Lifetime Cost")
            .enterCardName(cardName)
            .clickSaveButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedCardDisplayed(cardName)).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShowOrHideNonApplicableFieldsDisplayed(cardName, "Show Non-Applicable Fields")).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getNonApplicableFields(cardName)).doesNotContain("Liability Insurance Cost", "Liability Insurance Factor");

        partsAndAssembliesDetailsPage.clickToViewOrHideNonApplicableFields(cardName, "Show Non-Applicable Fields");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getNonApplicableFields(cardName)).contains("Liability Insurance Cost", "Liability Insurance Factor");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isShowOrHideNonApplicableFieldsDisplayed(cardName, "Hide Non-Applicable Fields")).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickToViewOrHideNonApplicableFields(cardName, "Hide Non-Applicable Fields")
            .deleteScenarioResultsCard(cardName);

        softAssertions.assertAll();
    }
}