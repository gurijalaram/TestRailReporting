package com.apriori.cis.ui.tests.partsandassembliesdetails;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.ScenarioStateEnum.COST_COMPLETE;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.utils.CisCostDetailsEnum;
import com.apriori.cis.ui.utils.CisDesignGuidanceDetailsEnum;
import com.apriori.cis.ui.utils.CisInsightsFieldsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartAndAssemblyViewInsightsTest extends TestBaseUI {
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions;
    private ScenariosUtil scenarioUtil;
    private ScenarioItem scenarioItem;
    private ComponentInfoBuilder componentInfoBuilder;
    private static final String componentName = "ChampferOut";

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        scenarioUtil = new ScenariosUtil();
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());
    }

    @Test
    @TestRail(id = {13283, 13144, 13145, 13146, 13284, 13165, 13151, 13162})
    @Description("Verify process routing cycle time details card")
    public void testProcessRoutingCycleTimeDetailsCard() {
        partsAndAssembliesDetailsPage.clickProcessRouting();

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

        softAssertions = partsAndAssembliesDetailsPage.verifyProcessDetails(softAssertions);
    }

    @Test
    @TestRail(id = {13163, 13164, 13296, 13166, 14541})
    @Description("Verify piece part cost details card")
    public void testProcessRoutingPiecePartCostDetailsCards() {
        partsAndAssembliesDetailsPage
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

        softAssertions = partsAndAssembliesDetailsPage.verifyProcessDetails(softAssertions);
    }

    @Test
    @TestRail(id = {13215, 13216, 13217, 13299, 14542})
    @Description("Verify fully burdened cost details card")
    public void testProcessRoutingFullyBurdenedCostDetailsCards() {
        partsAndAssembliesDetailsPage
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

        softAssertions = partsAndAssembliesDetailsPage.verifyProcessDetails(softAssertions);
    }

    @Test
    @TestRail(id = {16672, 16673, 16838, 16839, 16840})
    @Description("Verify user can add/remove process details fields")
    public void testEnableConfigurationsOfProcessDetailsCard() {
        partsAndAssembliesDetailsPage
            .clickProcessRouting()
            .selectProcess("Cycle Time")
            .clickCycleTimeChart();

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

        softAssertions = partsAndAssembliesDetailsPage.verifyProcessDetails(softAssertions);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Total Machine Cost")).isNotEmpty();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getProcessDetails("Labor Time")).isNotEmpty();
        partsAndAssembliesDetailsPage.resetToDefaultConfiguration();
    }

    @Test
    @TestRail(id = {17093, 17094, 17095, 17096, 17098, 17099})
    @Description("Verify discussions can create from process details card")
    public void testCreateDiscussionsFromProcessDetailsCard() {
        partsAndAssembliesDetailsPage
            .clickProcessRouting()
            .selectProcess("Cycle Time")
            .clickCycleTimeChart();

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
    }

    @Test
    @TestRail(id = {13320, 13321, 13322, 13323, 13324, 13375, 13376, 13377})
    @Description("Verify design guidance issue details")
    public void testDesignGuidanceIssueDetails() {
        partsAndAssembliesDetailsPage
            .clickDesignGuidance()
            .clickIssueCollapsibleIcon();

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
    }

    @Test
    @TestRail(id = {13584, 13585, 13586, 13588, 13589, 13590})
    @Description("Verify design guidance investigation section details")
    public void testDesignGuidanceInvestigationDetails() {
        partsAndAssembliesDetailsPage
            .clickDesignGuidance()
            .clickInvestigationCollapsibleIcon();

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
    }

    @Test
    @TestRail(id = {14033, 14034, 14035, 14036, 14037, 14038, 14039})
    @Description("Verify design guidance threads section details")
    public void testDesignGuidanceThreadsDetails() {
        partsAndAssembliesDetailsPage
            .clickDesignGuidance()
            .clickThreadsCollapsibleIcon();

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
    }

    @Test
    @TestRail(id = {13799, 13800, 13801, 13992, 13993, 13996, 13997})
    @Description("Verify cost details for part")
    public void testPartCostCards() {
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

    }

    @Test
    @TestRail(id = {12910, 12914})
    @Description("Verify insights section and details for a costed scenario")
    public void testInsightsSection() {
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderTitleOnInsights()).isEqualTo("Insights");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isProcessRoutingMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockMenuIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesMenuIconDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {12912, 12913, 12915, 12916, 13179})
    @Description("Verify part nesting section on Insights")
    public void testPartNestingSection() {
        partsAndAssembliesDetailsPage.clickPartNestingIcon();

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
    }

    @Test
    @TestRail(id = {13043, 13044, 13045, 13046})
    @Description("Verify material properties section on Insights")
    public void testMaterialPropertiesSection() {
        partsAndAssembliesDetailsPage.clickMaterialPropertiesIcon();

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
    }

    @Test
    @TestRail(id = {13047, 13048, 13049, 13050})
    @Description("Verify material stock section on Insights")
    public void testMaterialStockSection() {
        partsAndAssembliesDetailsPage.clickMaterialStockIcon();

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
    }

    @AfterEach
    public void testCleanUp() {
        softAssertions.assertAll();
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + COST_COMPLETE).get(0);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}
