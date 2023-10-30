package com.apriori.help;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Customer;
import com.apriori.pageobjects.compare.CompareExplorePage;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReportAnIssueTests extends TestBaseUI {
    AssemblyUtils asmUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private com.apriori.pageobjects.help.ReportAnIssue reportPage;
    private UserCredentials currentUser;
    private Customer customerDetails;


    @Test
    @TestRail(id = {28378, 28379, 28382, 28383, 28384})
    @Description("Verify contents of Report an issue modal from various views")
    public void testReportAnIssueFieldValues() {
        currentUser = UserUtil.getUser();
        customerDetails = AuthorizationUtil.getCurrentCustomerData();

        final String asmName = "titan charger ass";
        final String asmExtension = ".SLDASM";
        final List<String> subComponentNames = Arrays.asList("titan charger base", "titan charger lead", "titan charger upper");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder titanChargeAsm = asmUtils.associateAssemblyAndSubComponents(
            asmName, asmExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames, subComponentExtension, subComponentProcessGroup, scenarioName, currentUser);
        asmUtils.uploadSubComponents(titanChargeAsm)
            .uploadAssembly(titanChargeAsm);

        loginPage = new CidAppLoginPage(driver);
        reportPage = loginPage.login(currentUser)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Explore");

        reportPage = reportPage.close(ExplorePage.class)
            .clickCompare(CompareExplorePage.class)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Comparisons");

        reportPage = reportPage.close(CompareExplorePage.class)
            .clickExplore()
            .multiSelectScenarios(subComponentNames.stream().map(name -> name + "," + scenarioName).collect(Collectors.toList()).toArray(String[]::new))
            .createComparison()
            .selectManualComparison()
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Comparisons");

        reportPage = reportPage.close(ComparePage.class)
            .clickExplore()
            .openScenario(titanChargeAsm.getSubComponents().get(0).getComponentName(), scenarioName)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Evaluate");
        verifyScenarioDetails(titanChargeAsm.getSubComponents().get(0), "PART");

        reportPage = reportPage.close(EvaluatePage.class)
            .clickExplore()
            .openScenario(titanChargeAsm.getComponentName(), scenarioName)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Evaluate");
        verifyScenarioDetails(titanChargeAsm, "ASSEMBLY");

        reportPage = reportPage.close(EvaluatePage.class)
            .clickExplore()
            .openScenario(titanChargeAsm.getComponentName(), scenarioName)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails("Evaluate");

        softAssertions.assertAll();

    }

    /**
     * Common assertions made across all pages under test
     *
     * @param expectedPageName
     */
    private void verifyCommonDetails(String expectedPageName) {
        softAssertions.assertThat(reportPage.getFieldValue("Name")).as("Verify User Name").isEqualTo(currentUser.getUsername());
        softAssertions.assertThat(reportPage.getFieldValue("Email")).as("Verify User Email").isEqualTo(currentUser.getEmail());
        softAssertions.assertThat(reportPage.getFieldValue("Customer")).as("Verify Customer Name").isEqualTo(customerDetails.getName());
        softAssertions.assertThat(reportPage.getFieldValue("Customer Identity")).as("Verify Customer Identity")
            .isEqualTo(customerDetails.getIdentity());

        //ToDo:- Complete and uncomment assertions when we can directly query the browser and customer information
        //softAssertions.assertThat(reportPage.getFieldValue("User Identity")).as("Verify User Identity")
        //    .isEqualTo();
        //softAssertions.assertThat(reportPage.getFieldValue("Browser Information")).as("Verify Browser Information")
        //    .isEqualTo(driverFactory.browser);
        //softAssertions.assertThat(reportPage.getFieldValue("Deployment")).as("Verify Deployment").isEqualTo();
        //softAssertions.assertThat(reportPage.getFieldValue("Installation")).as("Verify Installation").isEqualTo();

        softAssertions.assertThat(reportPage.getFieldValue("Application")).as("Verify Application").isEqualTo("aP Design");
        softAssertions.assertThat(reportPage.getFieldValue("Page")).as("Verify Page").isEqualTo(expectedPageName);
    }

    /**
     * Common assertions made across all pages under test
     *
     * @param scenario - ComponentInfoBuilder of scenario details to be verified
     * @param scenarioType - String of expected scenarios type [Part | Assembly]
     */
    private void verifyScenarioDetails(ComponentInfoBuilder scenario, String scenarioType) {
        softAssertions.assertThat(reportPage.getFieldValue("Component Type")).as("Verify Component Type").isEqualTo(scenarioType);
        softAssertions.assertThat(reportPage.getFieldValue("Component Name")).as("Verify Component Name").isEqualTo(scenario.getComponentName());
        softAssertions.assertThat(reportPage.getFieldValue("Component Identity")).as("Verify Component Identity")
            .isEqualTo(scenario.getComponentIdentity());
        softAssertions.assertThat(reportPage.getFieldValue("Scenario Name")).as("Verify Scenario Name")
            .isEqualTo(scenario.getScenarioName());
        softAssertions.assertThat(reportPage.getFieldValue("Scenario Identity")).as("Verify Scenario Identity")
            .isEqualTo(scenario.getScenarioIdentity());

        //ToDo:- Uncomment and complete assertions once we can access iteration information
        //softAssertions.assertThat(reportPage.getFieldValue("Iteration Index")).as("Verify Iteration Index")
        //    .isEqualTo();
        //softAssertions.assertThat(reportPage.getFieldValue("Iteration Identity")).as("Verify Iteration Identity")
        //    .isEqualTo();
    }
}
