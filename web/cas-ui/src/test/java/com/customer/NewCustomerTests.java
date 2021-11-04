package com.customer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NewCustomerTests extends TestBase {
    private CustomerProfilePage customerProfilePage;

    @Before
    public void setup() {
        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .createNewCustomer();
    }

    @Test
    @Description("Clicking the New Customer button takes me to the profile form and all displays are valid.")
    @TestRail(testCaseId = "C9600")
    public void testValidateThatTheFormLabelsAreCorrect() {

        assertThat(customerProfilePage.isProfileTabDisplayed(), is(equalTo(true)));
        assertThat(customerProfilePage.isUsersTabEnabled(), is(equalTo(false)));
        assertThat(customerProfilePage.isSitesAndLicensesEnabled(), is(equalTo(false)));
        assertThat(customerProfilePage.isInfrastructureEnabled(), is(equalTo(false)));
        assertThat(customerProfilePage.isSecurityEnabled(), is(equalTo(false)));
        assertThat(customerProfilePage.isSystemConfigurationEnabled(), is(equalTo(false)));
        testNewCustomerLabelAvailable("Customer Name:");
        testNewCustomerLabelAvailable("Description:");
        testNewCustomerLabelAvailable("Customer Type:");
        testNewCustomerLabelAvailable("Salesforce ID:");
        testNewCustomerLabelAvailable("Cloud Reference:");
        testNewCustomerLabelAvailable("Email Domains:");
        testNewCustomerLabelAvailable("CAD File Retention Policy (days):");
        testNewCustomerLabelAvailable("Max CAD File Size:");
        testNewCustomerLabelAvailable("Last Updated:");
        testNewCustomerLabelAvailable("Updated By:");
        testNewCustomerLabelAvailable("Created:");
        testNewCustomerLabelAvailable("Created By:");
        testNewCustomerLabelAvailable("Authentication:");
    }

    private void testNewCustomerLabelAvailable(String label) {
        String query = String.format("//label[.='%s']", label);
        WebElement element = driver.findElement(By.xpath(query));
        boolean actual = element.isDisplayed();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("Clicking the cancel button returns the user to the customer list.")
    @TestRail(testCaseId = "C9613")
    public void testCancelReturnsTheUserToTheCustomerListWhenCreatingANewCustomer() {

        CustomerAdminPage actual = customerProfilePage.cancel(CustomerAdminPage.class);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    @Description("The save button creates the new user and reloads the page in edit mode.")
    @TestRail(testCaseId = "C9616")
    public void testSaveCreatesTheNewUserAndThePageReloadsWithTheNewCustomer() {

        DateFormat format = new SimpleDateFormat("0yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String salesforceId = format.format(new Date());
        String customerName = String.format("QA Automation %s", salesforceId);
        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName);

        assertThat(customerProfilePage.isSaveButtonEnabled(), is(equalTo(true)));
        customerProfilePage.enterCustomerName(null);
        assertThat(customerProfilePage.isSaveButtonEnabled(), is(equalTo(false)));

        CustomerProfilePage editPage = customerProfilePage
            .enterCustomerName(customerName)
            .save()
            .edit();

        assertThat(editPage, is(notNullValue()));
    }

    @Test
    @Description("All enabled fields are required.")
    @TestRail(testCaseId = "C9617")
    public void testTheNecessaryFieldsAreRequired() {

        customerProfilePage.enterCustomerName(null);
        assertThat(customerProfilePage.getCustomerNameFeedback(), is(equalTo("Enter a customer name. This should be the company's official name or the name in Salesforce.")));
        customerProfilePage.enterDescription(null);
        assertThat(customerProfilePage.getDescriptionFeedback(), is(equalTo("Enter a description for the customer.")));
        customerProfilePage.clearCustomerType();
        assertThat(customerProfilePage.getCustomerTypeFeedback(), is(equalTo("Select the customer type.")));
        customerProfilePage.enterSalesforceId(null);
        assertThat(customerProfilePage.getSalesforceIdFeedback(), is(equalTo("Please enter the salesforce id.")));
        customerProfilePage.enterEmailDomains(null);
        assertThat(customerProfilePage.getEmailDomFeedback(), is(equalTo("Enter email patterns using \",\" between items.")));
        customerProfilePage.enterCadFileRetentionPolicy(null);
        assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback(), is(equalTo("Enter the CAD retention policy.")));
        customerProfilePage.enterMaxCadFileSize(null);
        assertThat(customerProfilePage.getMaxCadFileSizeFeedback(), is(equalTo("Enter the max CAD file size.")));
    }

    @Test
    @Description("Customer name should be no more than 64 characters in length.")
    @TestRail(testCaseId = "C9618")
    public void testCustomerNameShouldBeNoMoreThan64Characters() {

        String nameTooLong = RandomStringUtils.randomAlphanumeric(65);
        String actual = customerProfilePage
            .enterCustomerName(nameTooLong)
            .getCustomerNameFeedback();
        assertThat(actual, is(equalTo("Should be no more than 64 characters in length.")));
    }

    @Test
    @Description("Salesforce ID should be 15 or 18 characters.")
    @TestRail(testCaseId = "C9622")
    public void testSalesforceIdShouldBe15Or18Characters() {

        String salesforceId = RandomStringUtils.randomNumeric(14);
        String actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Should be 15 or 18 characters.")));

        salesforceId = RandomStringUtils.randomNumeric(16);
        actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Should be 15 or 18 characters.")));

        salesforceId = RandomStringUtils.randomNumeric(17);
        actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Should be 15 or 18 characters.")));

        salesforceId = RandomStringUtils.randomNumeric(19);
        actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Should be 15 or 18 characters.")));

    }

    @Test
    @Description("Cloud reference should be disabled for on premise customers and required for cloud customers.")
    @TestRail(testCaseId = "C9623")
    public void testCloudReferenceIsDisabledForOnPremiseCustomers() {

        customerProfilePage.selectCustomerTypeOnPremise();
        assertThat(customerProfilePage.isCloudReferenceEnabled(), is(equalTo(false)));
        customerProfilePage.selectCustomerTypeCloud();
        assertThat(customerProfilePage.isCloudReferenceEnabled(), is(equalTo(true)));
        customerProfilePage.selectCustomerTypeOnPremiseAndCloud();
        assertThat(customerProfilePage.isCloudReferenceEnabled(), is(equalTo(true)));
        customerProfilePage.enterCloudRef(null);
        assertThat(customerProfilePage.getCloudRefFeedback(),  is(equalTo("Enter a cloud reference.")));
        customerProfilePage.selectCustomerTypeOnPremise();
        assertThat(customerProfilePage.getCloudRefFeedback(), is(equalTo("")));
        assertThat(customerProfilePage.getCloudRefValue(), is(equalTo("")));
        assertThat(customerProfilePage.isCloudReferenceEnabled(), is(equalTo(false)));
    }


    @Test
    @Description("Invalid emails should not be allowed.")
    @TestRail(testCaseId = "C9629")
    public void testEmailDomainsRequireValidEmails() {

        customerProfilePage.enterEmailDomains("aa");
        assertThat(customerProfilePage.getEmailDomFeedback(), is(equalTo("Email domains must be comma separated, contain no spaces, and must include the top level domain of at least 2 characters.")));
    }

    @Test
    @Description("CAD File Retention Policy requires at least one day and has a maximum of 1095 days.")
    @TestRail(testCaseId = "C9631")
    public void testCadFileRetentionPolicyRequiresAtLeastOneDayAndAtMost1095Days() {

        customerProfilePage.enterCadFileRetentionPolicy("0");
        assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback(), is(equalTo("The retention policy requires at least 1 day.")));
        customerProfilePage.enterCadFileRetentionPolicy("1096");
        assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback(), is(equalTo("The retention policy allows at most 1095 days.")));
    }

    @Test
    @Description("Max CAD File Size should be between 10 MB and 100MB.")
    @TestRail(testCaseId = "C9634")
    public void testMaxCadFileSizeShouldBeAtLeast10MbAndAtMost100MB() {

        customerProfilePage.enterMaxCadFileSize("9");
        assertThat(customerProfilePage.getMaxCadFileSizeFeedback(), is(equalTo("You will require at least 10 MB for a CAD file.")));
        customerProfilePage.enterMaxCadFileSize("101");
        assertThat(customerProfilePage.getMaxCadFileSizeFeedback(), is(equalTo("The maximum CAD file size cannot exceed 100 MB.")));
    }
}
