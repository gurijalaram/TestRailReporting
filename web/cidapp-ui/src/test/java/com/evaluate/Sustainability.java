package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class Sustainability extends TestBase {
    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private File resourceFile;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private MaterialUtilizationPage materialUtilizationPage;

    @Test
    @TestRail(testCaseId = {"24103","24100"})
    @Description("Verify if Sustainability tab is presented on Evaluate page and Material Carbon is presented in Material Utilization tab")
    public void materialUtilHelp() {
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

        materialUtilizationPage =
        evaluatePage.openMaterialProcess()
                .openMaterialUtilizationTab();

        softAssertions.assertThat(materialUtilizationPage.isMaterialCarbonPresent()).isTrue();
        softAssertions.assertAll();



    }
}
