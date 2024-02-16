package com.apriori.cis.ui.tests.partsandassembliesdetails;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.ScenarioStateEnum.COST_COMPLETE;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.utils.CisColumnsEnum;
import com.apriori.cis.ui.utils.CisCostDetailsEnum;
import com.apriori.cis.ui.utils.CisScenarioResultsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private SoftAssertions softAssertions;
    private ScenariosUtil scenarioUtil;
    private ScenarioItem scenarioItem;
    private ComponentInfoBuilder componentInfoBuilder;
    private static final String componentName = "ChampferOut";

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        scenarioUtil = new ScenariosUtil();
    }

    @Test
    @TestRail(id = {12254, 12396, 12459, 12460, 12461})
    @Description("Verify 3D viewer and column cards on parts and assemblies details page")
    public void testPartsAndAssembliesDetailPageHeaderTitle() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver)
            .login(currentUser, LeftHandNavigationBar.class)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getHeaderText()).isEqualTo(componentInfoBuilder.getComponentName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.is3DCadViewerToolBarDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isScenarioResultsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isInsightsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentsCardDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartsAndAssembliesLinkDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {13067, 13068, 13069, 13070, 13071, 13072})
    @Description("Verify Scenario Results Default Cards on parts and assemblies details page")
    public void testScenarioResultsDefaultCards() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

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
    }

    @Test
    @TestRail(id = {13180, 13182, 13197, 13924})
    @Description("Verify insights for a non-costed scenario")
    public void testInsightsSectionForUnCostedScenario() {
        String componentName = "bluesky-latch-cover";
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.CASTING_DIE);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isPartNestingMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialPropertiesMenuIconDisplayed()).isEqualTo(false);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMaterialStockMenuIconDisplayed()).isEqualTo(false);

        partsAndAssembliesDetailsPage.clickCostsOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getUnCostMessage()).contains("Cost the scenario to see the cost result summary.");
    }

    @Test
    @TestRail(id = {12485, 12486, 12487, 12489, 12490, 12491, 12492, 12495})
    @Description("Verify the creation of new scenario results cards")
    public void testCreateNewScenarioResultsCards() {
        String cardName = "Process Cost Card";
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

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
    }

    @Test
    @TestRail(id = {12499, 12500, 12502, 12503, 12507, 12509, 12510, 13088, 13089, 13090})
    @Description("Verify scenario results cards can be edited")
    public void testEditScenarioResultsCards() {
        String cardName = "Process Cost Card";
        String editedCardName = "Process Analysis Card";
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName())
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
    }

    @Test
    @TestRail(id = {12523, 12524, 12525, 12527})
    @Description("Verify scenario results cards can be removed")
    public void testRemoveScenarioResultsCards() {
        String cardName = "Process Cost Card";
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName())
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

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeViewDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns());

        partsAndAssembliesDetailsPage.clickShowHideOption()
            .hideField("State");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).doesNotContain(CisColumnsEnum.STATE.getColumns());

        partsAndAssembliesDetailsPage.openAssembly("Pin", scenarioName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubComponentName()).isEqualTo("Pin");
    }

    @Test
    @TestRail(id = {13270, 13271, 13272, 13273, 13274, 13275, 13276})
    @Description("Verify share scenario functionalities")
    public void testShareScenarioFunction() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

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
    }

    @Test
    @TestRail(id = {13307, 13308, 13309, 13310, 13314, 13315, 13316, 14180})
    @Description("Verify that discussions can be created by scenario results attributes")
    public void testCreateDiscussionByScenarioAttributes() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

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
    }

    @Test
    @TestRail(id = {14050, 14051, 14052, 14053})
    @Description("Verify remove participants functionalities")
    public void testRemoveParticipants() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName())
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
    }

    @Test
    @TestRail(id = {16676, 16678})
    @Description("Verify user can open the same component in CID app from details view")
    public void testOpenComponentInCID() {
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isOpenInCIDButtonDisplayed()).isEqualTo(true);

        evaluatePage = partsAndAssembliesDetailsPage.clickOnOpenComponent()
            .clickOnCid()
            .switchTab();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(componentInfoBuilder.getScenarioName())).isEqualTo(true);
    }

    @Test
    @TestRail(id = {16460, 16461, 16462, 16463})
    @Description("Verify non-applicable fields are hidden in scenario info cards")
    public void testShowHideNonApplicableFieldsInScenarioInfoCards() {
        String cardName = "Liability Inputs";
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());

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
    }

    @Test
    @TestRail(id = {14040, 14041, 14042, 14043, 14044, 14045, 14046, 14047})
    @Description("Verify assembly cost details breakdowns")
    public void testAssemblyCostDetails() {
        componentInfoBuilder = new ComponentInfoBuilder();
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        componentInfoBuilder.setScenarioName(scenarioName);
        componentInfoBuilder.setComponentName(assemblyName);
        componentInfoBuilder.setUser(currentUser);
        partsAndAssembliesPage = new CisLoginPage(driver)
            .cisLogin(currentUser)
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
    }

    @AfterEach
    public void testCleanUp() {
        softAssertions.assertAll();
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + COST_COMPLETE).get(0);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}