package com.apriori.help;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Customer;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class ReportAnIssueTests extends TestBaseUI {
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
        loginPage = new CidAppLoginPage(driver);
        reportPage = loginPage.login(currentUser)
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails();
        softAssertions.assertThat(reportPage.getFieldValue("Page")).as("Verify Page").isEqualTo("Explore");

        reportPage = reportPage.close(ExplorePage.class)
            .clickCompare()
            .goToHelp()
            .clickReportAnIssue();

        verifyCommonDetails();
        softAssertions.assertThat(reportPage.getFieldValue("Page")).as("Verify Page").isEqualTo("Comparisons");


        softAssertions.assertAll();

    }

    private void verifyCommonDetails() {
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
    }
}
