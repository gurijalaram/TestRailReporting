package com.apriori.help;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Customer;
import com.apriori.pageobjects.compare.CompareExplorePage;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Array;
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
            .multiSelectScenarios(subComponentNames.stream().map(name -> name + "," + scenarioName).collect(Collectors.toList()));
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
//        softAssertions.assertThat(reportPage.getFieldValue("User Identity")).as("Verify User Identity")
//            .isEqualTo();

//        softAssertions.assertThat(reportPage.getFieldValue("Browser Information")).as("Verify Browser Information")
//            .isEqualTo());
//        softAssertions.assertThat(reportPage.getFieldValue("Deployment")).as("Verify Deployment").isEqualTo();
//        softAssertions.assertThat(reportPage.getFieldValue("Installation")).as("Verify Installation").isEqualTo();
        softAssertions.assertThat(reportPage.getFieldValue("Application")).as("Verify Application").isEqualTo("aP Design");
        softAssertions.assertThat(reportPage.getFieldValue("Page")).as("Verify Page").isEqualTo(expectedPageName);
    }
}
