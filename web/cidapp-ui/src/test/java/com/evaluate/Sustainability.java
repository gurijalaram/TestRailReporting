package com.evaluate;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class Sustainability extends TestBaseUI {
    UserCredentials currentUser;
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

        softAssertions.assertThat(evaluatePage.isSustainabilityTabIsPresentedForCosted()).isTrue();

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
