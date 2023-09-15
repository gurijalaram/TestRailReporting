package com.apriori.help;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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


    @Test
    @TestRail(id = {263, 6370, 6691, 6693})
    @Description("Verify contents of Report an issue modal from Explore and Comparison views")
    public void testReportAnIssueFieldValues() {
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        reportPage = loginPage.login(currentUser)
            .goToHelp()
            .clickReportAnIssue();

        softAssertions.assertThat(reportPage.getFieldValue("Name")).as("Verify User Name").isEqualTo(currentUser.getUsername());
        softAssertions.assertThat(reportPage.getFieldValue("Email")).as("Verify User Email").isEqualTo(currentUser.getEmail());
//        softAssertions.assertThat(reportPage.getFieldValue("Customer")).as("Verify Customer Name").isEqualTo();
//        softAssertions.assertThat(reportPage.getFieldValue("Customer Identity")).as("Verify Customer Identity").isEqualTo(currentUser.getUsername());
//        softAssertions.assertThat(reportPage.getFieldValue("User Identity")).as("Verify User Identity").isEqualTo(currentUser.get());

        softAssertions.assertAll();

    }
}
