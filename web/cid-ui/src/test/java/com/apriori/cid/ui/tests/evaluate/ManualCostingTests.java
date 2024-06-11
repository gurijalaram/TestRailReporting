package com.apriori.cid.ui.tests.evaluate;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.IterationsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.SwitchCostModePage;
import com.apriori.cid.ui.utils.CurrencyEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.CostRollupOverrides;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ManualCostingTests  extends TestBaseUI {

    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private SwitchCostModePage switchCostModePage;

    private static ScenariosUtil scenariosUtil = new ScenariosUtil();
    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static IterationsUtil iterationsUtil = new IterationsUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    private ComponentInfoBuilder component;

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @TestRail(id = {30102, 30104, 30105, 30107, 30108, 30109, 30110, 30111, 30112})
    @Description("Verify Cost Mode can be toggled")
    public void testToggleCostingModes() {
        component = new ComponentRequestUtil().getComponent();

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isSimulateCostModeSelected()).as("Verify Cost Mode is aPriori by default").isTrue();

        evaluatePage.clickManualModeButtonWhileUncosted()
            .enterPiecePartCost("42")
            .enterTotalCapitalInvestment("316");

        softAssertions.assertThat(evaluatePage.isManualCostModeSelected()).as("Verify switch to manual mode").isTrue();
        softAssertions.assertThat(evaluatePage.isSaveButtonEnabled()).as("Verify Save button currently disabled").isTrue();
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UNSAVED)).isEqualTo(true);

        evaluatePage.clickSimulateModeButton()
            .clickCancel();

        softAssertions.assertThat(evaluatePage.isManualCostModeSelected()).as("Verify cancel returns to manual mode").isTrue();

        switchCostModePage = evaluatePage.clickSimulateModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO APRIORI MODE");

        evaluatePage = switchCostModePage.clickContinue();
        softAssertions.assertThat(evaluatePage.isSimulateCostModeSelected()).as("Verify Cost Mode is toggled back to aPriori").isTrue();

        switchCostModePage = evaluatePage.enterAnnualVolume("5501")
                .clickManualModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO MANUAL MODE");

        evaluatePage = switchCostModePage.clickContinue()
                .clickSimulateModeButton()
                .clickContinue();

        softAssertions.assertThat(evaluatePage.getAnnualVolume()).as("Verify uncosted changes not retained").isNotEqualTo("5501");

        switchCostModePage = evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_FINLAND)
            .enterAnnualVolume("7777")
            .costScenario()
            .clickManualModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO MANUAL MODE");

        evaluatePage = switchCostModePage.clickCancel();

        softAssertions.assertThat(evaluatePage.isSimulateCostModeSelected()).as("Verify cancel returns to aPriori mode").isTrue();
        softAssertions.assertThat(evaluatePage.getDigitalFactory())
            .as("Verify uncosted changes retained after cancel").isEqualTo(DigitalFactoryEnum.APRIORI_FINLAND.getDigitalFactory());
        softAssertions.assertThat(evaluatePage.getAnnualVolume())
            .as("Verify uncosted changes retained after cancel").isEqualTo("7777");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30106, 30598, 30591})
    @Description("Test Validation of Cost Mode toggle and Manual Cost Inputs")
    public void testValidations() {
        String negativeNumberError = "Must be greater than or equal to 0.";
        component = new ComponentRequestUtil().getComponent();

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isCostModeToggleEnabled()).as("Verify Cost Mode toggle is enabled").isTrue();

        evaluatePage.clickActions().lock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).as("Verify scenario has been locked").isTrue();
        softAssertions.assertThat(evaluatePage.isCostModeToggleEnabled()).as("Verify Cost Mode toggle is disabled when scenario locked").isFalse();

        evaluatePage.clickActions().unlock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK)).as("Verify scenario has been unlocked").isTrue();
        softAssertions.assertThat(evaluatePage.isCostModeToggleEnabled()).as("Verify Cost Mode toggle is enabled when scenario unlocked").isTrue();

        evaluatePage.clickManualModeButtonWhileUncosted();

        ChangeSummaryPage changeSummary = evaluatePage.changeSummaryManual();

        softAssertions.assertThat(changeSummary.getChangedFrom("Cost Mode")).as("Verify Cost Mode in Change Summary").isEqualTo("SIMULATE");
        softAssertions.assertThat(changeSummary.getChangedTo("Cost Mode")).as("Verify Cost Mode in Change Summary").isEqualTo("MANUAL");

        evaluatePage = changeSummary.close(EvaluatePage.class);

        evaluatePage.enterPiecePartCost("-1");
        softAssertions.assertThat(evaluatePage.getPiecePartCostErrorText()).as("Verify that non negative values allowed").isEqualTo(negativeNumberError);
        evaluatePage.enterPiecePartCost("");

        evaluatePage.enterTotalCapitalInvestment("-1");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestmentErrorText()).as("Verify that non negative values allowed").isEqualTo(negativeNumberError);
        evaluatePage.enterTotalCapitalInvestment("");

        evaluatePage.enterPiecePartCost("-0.9");
        softAssertions.assertThat(evaluatePage.getPiecePartCostErrorText()).as("Verify that non negative values allowed").isEqualTo(negativeNumberError);
        evaluatePage.enterPiecePartCost("");

        evaluatePage.enterTotalCapitalInvestment("-0.04");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestmentErrorText()).as("Verify that non negative values allowed").isEqualTo(negativeNumberError);
        evaluatePage.enterTotalCapitalInvestment("");

        evaluatePage.enterPiecePartCost("'Forty Two(){}<>");
        evaluatePage.enterTotalCapitalInvestment("'Forty Two(){}<>");
        softAssertions.assertThat(evaluatePage.getPiecePartCost()).as("Verify non numerical values not accepted").isEqualTo("");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestment()).as("Verify non numerical values not accepted").isEqualTo("");

        evaluatePage.enterPiecePartCost("1.23");
        evaluatePage.enterTotalCapitalInvestment("4.56");

        changeSummary = evaluatePage.changeSummaryManual();

        softAssertions.assertThat(changeSummary.getChangedFrom("Cost Mode")).as("Verify Cost Mode in Change Summary").isEqualTo("SIMULATE");
        softAssertions.assertThat(changeSummary.getChangedTo("Cost Mode")).as("Verify Cost Mode in Change Summary").isEqualTo("MANUAL");
        softAssertions.assertThat(changeSummary.getChangedFrom("Cost Results-piecePartCost"))
            .as("Verify Piece Part Cost in Change Summary").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedFrom("Cost Results-totalCapitalInvestment"))
            .as("Verify Cost Mode in Change Summary").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Cost Results-piecePartCost"))
            .as("Verify Piece Part Cost in Change Summary").isEqualTo("1.23");
        softAssertions.assertThat(changeSummary.getChangedTo("Cost Results-totalCapitalInvestment"))
            .as("Verify Cost Mode in Change Summary").isEqualTo("4.56");

        evaluatePage = changeSummary.close(EvaluatePage.class)
            .openSettings()
            .selectCurrency(CurrencyEnum.EUR)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getPiecePartCostLabelText())
            .as("Verify Currency updates to that set in Preferences").contains(CurrencyEnum.EUR.getCurrency().split(" ")[0]);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31058, 31059, 31060, 31062, 31064})
    @Description("Test Scenario can be Manually Costed via UI")
    public void testPerformManualCost() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        componentsUtil.postComponent(component);
        scenariosUtil.postCostScenario(component);

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .openScenario(component.getComponentName(), component.getScenarioName());

        evaluatePage.clickManualModeButton()
            .clickContinue()
            .clickSaveButton();

        ResponseWrapper<ComponentIteration> results = iterationsUtil.getComponentIterationLatest(component);
        softAssertions.assertThat(evaluatePage.getPiecePartCost())
                .as("Verify default value for Piece Part Cost is an empty field").isEqualTo("");
        softAssertions.assertThat(results.getResponseEntity().getCostingInput().getCostRollupOverrides().getPiecePartCost())
            .as("Verify that default value for Piece Part Cost saves as 0.0").isEqualTo(null);
        softAssertions.assertThat(evaluatePage.getPiecePartCost())
            .as("Verify default value for Total Capital Investment is an empty field").isEqualTo("");
        softAssertions.assertThat(results.getResponseEntity().getCostingInput().getCostRollupOverrides().getTotalCapitalInvestment())
            .as("Verify that default value for Total Capital Investment saves as 0.0").isEqualTo(null);

        evaluatePage.enterPiecePartCost("15.98")
            .enterTotalCapitalInvestment("480.31")
            .clickSaveButton();

        results = iterationsUtil.getComponentIterationLatest(component);
        softAssertions.assertThat(evaluatePage.getPiecePartCost())
            .as("Verify value for Piece Part Cost is as set").isEqualTo("15.98");
        softAssertions.assertThat(results.getResponseEntity().getCostingInput().getCostRollupOverrides().getPiecePartCost())
            .as("Verify that value for Piece Part Cost saved").isEqualTo(15.98);
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestment())
            .as("Verify value for Total Capital Investment is as set").isEqualTo("480.31");
        softAssertions.assertThat(results.getResponseEntity().getCostingInput().getCostRollupOverrides().getTotalCapitalInvestment())
            .as("Verify that value for Total Capital Investment saved").isEqualTo(480.31);

        evaluatePage.clickSimulateModeButton()
            .clickContinue()
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).as("Verify Cost Failed as expected").isTrue();

        evaluatePage.clickManualModeButton()
            .clickContinue()
            .enterPiecePartCost("10.99")
            .enterTotalCapitalInvestment("103.97")
            .clickSaveButton();

        softAssertions.assertThat(evaluatePage.getPiecePartCost())
            .as("Verify value for Piece Part Cost is as set").isEqualTo("10.99");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestment())
            .as("Verify value for Total Capital Investment is as set").isEqualTo("103.97");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31009, 31010, 31017, 31018, 31020, 31021, 31022})
    @Description("Verify all actions can be performed on Manually Costed Scenario")
    public void testActionsForManuallyCostedScenarios() {
        String copiedScenarioName = new GenerateStringUtil().generateScenarioName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);
        componentsUtil.postComponent(component);
        component.setCostingTemplate(CostingTemplate.builder()
            .costMode("MANUAL")
            .costRollupOverrides(CostRollupOverrides.builder()
                .piecePartCost(16.75)
                .totalCapitalInvestment(98.34)
                .build())
            .build());
        scenariosUtil.postCostScenario(component);

        ComponentInfoBuilder component2 = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);
        component2.setUser(component.getUser());
        componentsUtil.postComponent(component2);
        scenariosUtil.postCostScenario(component2);

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .openScenario(component.getComponentName(), component.getScenarioName());

        evaluatePage.clickActions()
            .updateCadFile(component.getResourceFile())
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_UPDATE_CAD)).as("Verify Update CAD status displayed").isTrue();
        evaluatePage.waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 2);

        evaluatePage.publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION)).as("Verify Publishing status displayed").isTrue();
        evaluatePage = evaluatePage.waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).as("Verify scenario is now Public").isTrue();
        softAssertions.assertThat(evaluatePage.isEditButtonEnabled()).as("Verify Edit button is displayed and enabled").isTrue();

        evaluatePage = evaluatePage.editScenario(EditScenarioStatusPage.class)
            .clickHere();

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).as("Verify scenario is now Private").isTrue();
        softAssertions.assertThat(evaluatePage.isPublishButtonEnabled()).as("Verify Publish button now displayed and enabled").isTrue();

        evaluatePage.copyScenario()
            .enterScenarioName(copiedScenarioName)
            .submit(EvaluatePage.class);

        ComponentInfoBuilder copiedScenario = scenariosUtil.getComponentDetails(component.getComponentName(), copiedScenarioName, component.getUser());

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify copied scenario now open").isEqualTo(copiedScenarioName);
        softAssertions.assertThat(evaluatePage.getPiecePartCost()).as("Verify PPC copied successfully").isEqualTo("16.75");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestment()).as("Verify TCI copied successfully").isEqualTo("98.34");

        explorePage = evaluatePage.clickExplore()
            .selectFilter("Recent");

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()))
            .as("Verify Public and Private copies exist").isEqualTo(2);

        explorePage.selectFilter("Private");

        ComponentBasicPage componentBasicPage = explorePage.multiSelectScenarios(
                    component.getComponentName() + "," + component.getScenarioName(),
                    component2.getComponentName() + "," + component2.getScenarioName())
                .clickCostButton(ComponentBasicPage.class);

        softAssertions.assertThat(componentBasicPage.getAlertMessage())
            .as("Verify warning that Manually Costed scenario included in selection")
            .isEqualTo("One or more of the selected components were last costed in Manual mode. If you choose to continue, they will be recosted in Simulate mode.");

        explorePage = componentBasicPage.applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        scenariosUtil.getScenarioCompleted(component);
        scenariosUtil.getScenarioCompleted(component2);

        evaluatePage = explorePage.openScenario(component.getComponentName(), component.getScenarioName());

        softAssertions.assertThat(evaluatePage.isSimulateCostModeSelected())
            .as("Verify Manually Costed scenario switched to Simulate on Group Cost").isTrue();

        explorePage = evaluatePage.clickExplore()
            .refresh()
            .multiSelectScenarios(
                copiedScenario.getComponentName() + "," + copiedScenario.getScenarioName(),
                component2.getComponentName() + "," + component2.getScenarioName())
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        scenariosUtil.getScenarioCompleted(copiedScenario);
        scenariosUtil.getScenarioCompleted(component2);

        evaluatePage = explorePage.refresh()
            .openScenario(copiedScenario.getComponentName(), copiedScenario.getScenarioName());

        softAssertions.assertThat(evaluatePage.isSimulateCostModeSelected())
            .as("Verify Manually Costed scenario switched to Simulate on Group Cost").isTrue();
        softAssertions.assertThat(evaluatePage.getDigitalFactory())
            .as("Verify previously selected Digital Factory used").isEqualTo(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());

        evaluatePage.clickManualModeButton()
            .clickContinue()
            .enterPiecePartCost("15.59")
            .enterTotalCapitalInvestment("64.31")
            .clickSaveButton();

        explorePage = evaluatePage.clickExplore()
            .multiSelectScenarios(copiedScenario.getComponentName() + "," + copiedScenario.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(copiedScenario)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(copiedScenario.getComponentName(), copiedScenario.getScenarioName()))
            .as("Verify scenario removed from explore page").isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30102, 30104, 30105, 30107, 30108, 30109, 30110, 30111, 30112})
    @Description("Verify Manually Costed Scenarios cannot be used in 2-Model Machining Source Model")
    public void testManuallyCostedAs2MMSource() {
        component = new ComponentRequestUtil().getComponent();

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickManualModeButtonWhileUncosted()
            .enterPiecePartCost("42")
            .enterTotalCapitalInvestment("316")
            .clickCostButton()
            .waitForCostLabelNotContain(NewCostingLabelEnum.SAVING_IN_PROGRESS, 2)
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class);
    }
}
