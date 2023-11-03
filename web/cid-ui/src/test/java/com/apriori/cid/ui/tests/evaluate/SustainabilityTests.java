package com.apriori.cid.ui.tests.evaluate;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class SustainabilityTests extends TestBaseUI {
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private File resourceFile;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private MaterialUtilizationPage materialUtilizationPage;
    private MaterialProcessPage materialProcessPage;

    @Test
    @TestRail(id = {24103, 24100, 24360})
    @Description("Verify if Sustainability tab is presented on Evaluate page, and Material Carbon, Energy Carbon properties are visible")
    public void sustainabilityPropertiesTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isSustainabilityDetailsPresentForCosted()).isTrue();
        softAssertions.assertThat(evaluatePage.getSustainabilityNames()).containsAll(Arrays.asList("Processes Missing Sustainability", "Material Carbon", "Process Carbon",
            "Logistics Carbon", "Total Carbon"));

        materialProcessPage = evaluatePage.openMaterialProcess();
        materialUtilizationPage = materialProcessPage.openMaterialUtilizationTab();
        softAssertions.assertThat(materialUtilizationPage.isMaterialCarbonPresent()).isTrue();

        materialProcessPage
            .selectProcessesTab()
            .selectBarChart("Compaction Pressing")
            .selectProcessTab();
        softAssertions.assertThat(materialProcessPage.isEnergyCarbonPresent()).isTrue();
        softAssertions.assertAll();
    }
}
