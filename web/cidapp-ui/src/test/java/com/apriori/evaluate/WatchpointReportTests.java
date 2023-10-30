package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WatchpointReportTests extends TestBaseUI {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;

    public WatchpointReportTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver)
            .navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isReportButtonEnabled()).isTrue();
        evaluatePage.clickReportDropdown();

        softAssertions.assertThat(evaluatePage.isDownloadButtonEnabled()).isFalse();

        evaluatePage.clickReportDropdown()
            .generateReport(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_REPORT_ACTION, 3)
            .downloadReport(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getDownloadedReport(cidComponentItem).length()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Issue("BA-2962")
    @TestRail(id = {28525, 28526})
    @Description("Generate and download a Assembly Cost Report")
    public void assemblyCostReport() {

        final String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String bigRing = "flange";
        final String pin = "nut";
        final String smallRing = "bolt";
        final List<String> subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        final String subComponentExtension = ".CATPart";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isReportButtonEnabled()).isTrue();
        evaluatePage.clickReportDropdown();

        softAssertions.assertThat(evaluatePage.isDownloadButtonEnabled()).isFalse();

        evaluatePage.clickReportDropdown()
            .generateReport(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_REPORT_ACTION, 3)
            .downloadReport(EvaluatePage.class);

        softAssertions.assertThat((Long) evaluatePage.getDownloadedReport(componentAssembly).length()).isGreaterThan(0);

        softAssertions.assertAll();
    }
}
