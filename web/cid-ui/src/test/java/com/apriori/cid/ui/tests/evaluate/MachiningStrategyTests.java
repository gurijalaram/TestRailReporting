package com.apriori.cid.ui.tests.evaluate;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class MachiningStrategyTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    @Test
    @TestRail(id = {14210, 14211, 14212, 14213, 14214, 14937, 14938, 14936})
    @Description("Verify Machining Strategy option made available when suitable Process Group selected")
    public void testMachiningStrategyOptionAvailable() {
        component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(false);
        evaluatePage.selectProcessGroup(component.getProcessGroup());

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);

        evaluatePage.selectMachineOptionsCheckbox()
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(true);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_DIE);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(false);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(false);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_INVESTMENT);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(false);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_SAND);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(false);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(false);

        evaluatePage.selectProcessGroup(component.getProcessGroup());
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(true);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.SHEET_METAL);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14794})
    @Description("Verify non-machinable PG can be used after costing with machinable PG")
    public void testCostWithNonMachinableAfterMachinablePG() {
        component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(false);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15421})
    @Description("Evaluate page - Machinable PG can be selected and part can be costed with Do not machine this part checked")
    public void testCostWithMachiningOptionSelected() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .selectMachineOptionsCheckbox()
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).doesNotContain("5 Axis Mill");

        evaluatePage.selectMachineOptionsCheckbox()
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("5 Axis Mill");

        softAssertions.assertAll();
    }
}
