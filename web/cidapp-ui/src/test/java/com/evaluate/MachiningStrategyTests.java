package com.evaluate;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class MachiningStrategyTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {14210, 14211, 14212, 14213, 14214, 14937, 14938, 14936})
    @Description("Verify Machining Strategy option made available when suitable Process Group selected")
    public void testMachiningStrategyOptionAvailable() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "big ring";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(false);
        evaluatePage.selectProcessGroup(processGroupEnum);

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

        evaluatePage.selectProcessGroup(processGroupEnum);
        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxSelected()).isEqualTo(true);

        evaluatePage.selectProcessGroup(ProcessGroupEnum.SHEET_METAL);

        softAssertions.assertThat(evaluatePage.isMachineOptionsCheckboxDisplayed()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14794})
    @Description("Verify non-machinable PG can be used after costing with machinable PG")
    public void testCostWithNonMachinableAfterMachinablePG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "Part0004";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".ipt");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
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
