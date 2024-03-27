package com.apriori.cid.ui.tests.evaluate;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.SwitchCostModePage;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class ManualCostingTests  extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SwitchCostModePage switchCostModePage;

    private static ScenariosUtil scenariosUtil = new ScenariosUtil();
    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    private ComponentInfoBuilder component;

    @Test
    @TestRail(id = {30104, 30105, 30107, 30108, 30110, 30112})
    @Description("Verify Cost Mode can be toggled")
    public void testToggleCostingModes() {
        component = new ComponentRequestUtil().getComponent();

        evaluatePage = new CidAppLoginPage(driver).login(component.getUser())
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isAPrioriCostModeSelected()).as("Verify Cost Mode is aPriori by default").isTrue();

        evaluatePage.clickManualModeButtonWhileUncosted()
            .enterPiecePartCost("42")
            .enterTotalCapitalInvestment("619");

        softAssertions.assertThat(evaluatePage.isManualCostModeSelected()).as("Verify switch to manual mode").isTrue();
        softAssertions.assertThat(evaluatePage.isSaveAsButtonEnabled()).as("Verify Save button currently disabled").isFalse();

        evaluatePage.clickAPrioriModeButton()
            .clickCancel();

        softAssertions.assertThat(evaluatePage.isManualCostModeSelected()).as("Verify cancel returns to manual mode").isTrue();

        switchCostModePage = evaluatePage.clickAPrioriModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO APRIORI MODE");

        evaluatePage = switchCostModePage.clickContinue();
        softAssertions.assertThat(evaluatePage.isAPrioriCostModeSelected()).as("Verify Cost Mode is toggled back to aPriori").isTrue();

        switchCostModePage = evaluatePage.enterAnnualVolume("5501")
                .clickManualModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO MANUAL MODE");

        //ToDo:- Move this cancel test to somewhere better
        evaluatePage = switchCostModePage.clickCancel();

        softAssertions.assertThat(evaluatePage.getAnnualVolume()).as("Verify uncosted changes not retained").isNotEqualTo("5501");

        switchCostModePage = evaluatePage.selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .clickManualModeButton();

        softAssertions.assertThat(switchCostModePage.continueButtonText())
            .as("Verify Continue to aPriori Mode button text").isEqualTo("CONTINUE TO MANUAL MODE");

        evaluatePage = switchCostModePage.clickCancel();

        softAssertions.assertThat(evaluatePage.isAPrioriCostModeSelected()).as("Verify cancel returns to aPriori mode").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30100, 30102, 30106})
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

        evaluatePage.enterPiecePartCost("'Forty Five(){}<>");
        evaluatePage.enterTotalCapitalInvestment("'Forty Five(){}<>");
        softAssertions.assertThat(evaluatePage.getPiecePartCost()).as("Verify non numerical values not accepted").isEqualTo("");
        softAssertions.assertThat(evaluatePage.getTotalCapitalInvestment()).as("Verify non numerical values not accepted").isEqualTo("");



        softAssertions.assertAll();
    }
}
