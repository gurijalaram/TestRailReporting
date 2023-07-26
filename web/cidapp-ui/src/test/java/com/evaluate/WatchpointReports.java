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
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class WatchpointReports extends TestBaseUI {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private UserCredentials currentUser;

    public WatchpointReports() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @Issue("BA-2962")
    @TestRail(id = {21933, 21934, 21940})
    @Description("Generate and download a Part Cost Report")
    public void partCostReport() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isReportButtonEnabled()).isTrue();
        evaluatePage.clickReportDropdown();

        softAssertions.assertThat(evaluatePage.isDownloadButtonEnabled()).isFalse();

        evaluatePage.clickReportDropdown()
            .generateReport(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_REPORT_ACTION, 3)
            .downloadReport(EvaluatePage.class);

        softAssertions.assertThat((Long) evaluatePage.getReportJQueryData().get("total")).isGreaterThan(0);

        softAssertions.assertAll();
    }
}
