package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class WatchpointReportTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    public WatchpointReportTests() {
        super();
    }

    @Test
    @Disabled("Ticket has been pulled from release")
    @Tag(SMOKE)
    @Issue("BA-2962")
    @TestRail(id = {21933, 21934, 21940})
    @Description("Generate and download a Part Cost Report")
    public void partCostReport() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isReportButtonEnabled()).isTrue();
        evaluatePage.clickReportDropdown();

        softAssertions.assertThat(evaluatePage.isDownloadButtonEnabled()).isFalse();

        evaluatePage.clickReportDropdown()
            .generateReport(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_REPORT_ACTION, 3)
            .downloadReport(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getDownloadedReport(component).length()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @Disabled("Ticket has been pulled from release")
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
