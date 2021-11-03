package com.customer;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class NewCustomerTests extends TestBase {
    private CustomerProfilePage customerProfilePage;

    @Before
    public void setup() {
        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .createNewCustomer();
    }

    @Test
    @Description("When the new customer button is clicked, I should be taken to the new customer form.")
    public void testNewCustomerButtonRoutesToNewCustomerForm() {

        boolean actual = customerProfilePage.isProfileTabDisplayed();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("When creating a new customer, the tabs should be disabled.")
    public void testNewCustomerTabsDisabled() {

        boolean actual = customerProfilePage.isUsersTabEnabled() ||
            customerProfilePage.isSitesAndLicensesEnabled() ||
            customerProfilePage.isInfrastructureEnabled() ||
            customerProfilePage.isSecurityEnabled() ||
            customerProfilePage.isSystemConfigurationEnabled();
        assertThat(actual, is(equalTo(false)));
    }

    private void testNewCustomerLabelAvailable(String label) {
        String query = String.format("//label[.='%s']", label);
        WebElement element = driver.findElement(By.xpath(query));
        boolean actual = element.isDisplayed();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("The label for customer name should be Customer Name:")
    public void testNewCustomerLabelCustomerNameAvailable() {
        testNewCustomerLabelAvailable("Customer Name:");
    }

    @Test
    @Description("The label for description should be Description:")
    public void testNewCustomerLabelDescriptionAvailable() {
        testNewCustomerLabelAvailable("Description:");
    }

    @Test
    @Description("The label for customer type should be Customer Type:")
    public void testNewCustomerLabelCustomerTypeAvailable() {
        testNewCustomerLabelAvailable("Customer Type:");
    }

    @Test
    @Description("The label for salesforce id should be Salesforce ID:")
    public void testNewCustomerLabelSalesforceIdAvailable() {
        testNewCustomerLabelAvailable("Salesforce ID:");
    }

    @Test
    @Description("The label for cloud reference id should be Cloud Reference:")
    public void testNewCustomerLabelCloudReferenceAvailable() {
        testNewCustomerLabelAvailable("Cloud Reference:");
    }

    @Test
    @Description("The label for email domains id should be Email Domains:")
    public void testNewCustomerLabelEmailDomainsAvailable() {
        testNewCustomerLabelAvailable("Email Domains:");
    }

    @Test
    @Description("The label for cad file retention policy id should be CAD File Retention Policy (days):")
    public void testNewCustomerLabelCadFileRetentionPolicyAvailable() {
        testNewCustomerLabelAvailable("CAD File Retention Policy (days):");
    }

    @Test
    @Description("The label for max cad file size should be Max CAD File Size:")
    public void testNewCustomerLabelMaxCadFileSizeAvailable() {
        testNewCustomerLabelAvailable("Max CAD File Size:");
    }

    @Test
    @Description("The label for updated at should be Last Updated:")
    public void testNewCustomerLabelUpdatedAtAvailable() {
        testNewCustomerLabelAvailable("Last Updated:");
    }

    @Test
    @Description("The label for updated by should be Updated By:")
    public void testNewCustomerLabelUpdatedByAvailable() {
        testNewCustomerLabelAvailable("Updated By:");
    }

    @Test
    @Description("The label for created at should be Created At:")
    public void testNewCustomerLabelCreatedAtAvailable() {
        testNewCustomerLabelAvailable("Created:");
    }

    @Test
    @Description("The label for created by should be Created By:")
    public void testNewCustomerLabelCreatedByAvailable() {
        testNewCustomerLabelAvailable("Created By:");
    }

    @Test
    @Description("The label for authentication should be Authentication:")
    public void testNewCustomerLabelAuthenticationAvailable() {
        testNewCustomerLabelAvailable("Authentication:");
    }

    @Test
    @Description("Clicking the cancel button returns the user to the customer list.")
    public void testCancelReturnsTheUserToTheCustomerList() {

        CustomerAdminPage actual = customerProfilePage.cancel(CustomerAdminPage.class);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    @Description("The save button is disabled when the form is invalid.")
    public void testSaveIsDisabledWhenTheFormIsInvalid() {

        boolean actual = customerProfilePage.enterDescription("Automation Test Customer")
            .enterSalesforceId("000000000000000")
            .enterEmailDomains("apriori.com")
            .enterCustomerName(null)
            .isSaveButtonEnabled();
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    @Description("The save button is enabled when all fields have been entered and are valid.")
    public void testSaveIsEnabledWhenAllOfTheFieldsAreValid() {

        boolean actual = customerProfilePage.enterCustomerName("QA Automation")
            .enterDescription("Automation Test Customer")
            .enterSalesforceId("000000000000000000")
            .enterEmailDomains("apriori.com")
            .isSaveButtonEnabled();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("The save button creates the new user and reloads the page in edit mode.")
    public void testSaveCreatesTheNewUserAndThePageReloadsWithTheNewCustomer() {

        DateFormat format = new SimpleDateFormat("0yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String salesforceId = format.format(new Date());
        String customerName = String.format("QA Automation %s", salesforceId);
        CustomerProfilePage editPage = customerProfilePage.enterCustomerName(customerName)
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .save()
            .edit();
        assertThat(editPage, is(notNullValue()));
    }

    private void assertValidationErrorIsPresent(String expected, String field) {


    }

    @Test
    @Description("Customer name is required.")
    public void testCustomerNameRequired() {

        String actual = customerProfilePage
            .enterCustomerName(null)
            .getCustomerNameFeedback();
        assertThat(actual, is(equalTo("Enter a customer name. This should be the company's official name or the name in Salesforce.")));
    }

    @Test
    @Description("Customer name should be no more than 64 characters in length.")
    public void testCustomerNameShouldBeNoMoreThan64Characters() {

        String nameTooLong = RandomStringUtils.randomAlphanumeric(65);
        String actual = customerProfilePage
            .enterCustomerName(nameTooLong)
            .getCustomerNameFeedback();
        assertThat(actual, is(equalTo("Should be no more than 64 characters in length.")));
    }

    @Test
    @Description("Description should be required.")
    public void testDescriptionShouldBeRequired() {

        String actual = customerProfilePage
            .enterDescription(null)
            .getDescriptionFeedback();
        assertThat(actual, is(equalTo("Enter a description for the customer.")));
    }

    @Test
    @Description("Customer type should be required.")
    public void testCustomerTypeShouldBeRequired() {

        String actual = customerProfilePage
            .clearCustomerType()
            .getCustomerTypeFeedback();
        assertThat(actual, is(equalTo("Select the customer type.")));
    }

    @Test
    @Description("Salesforce ID is required.")
    public void testSalesforceIdIsRequired() {

        String actual = customerProfilePage
            .enterSalesforceId(null)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Please enter the salesforce id.")));
    }

    @Test
    @Description("Salesforce ID should be 15 or 18 characters.")
    public void testSalesforceIdShouldBe15Or18Characters() {

        String salesforceId = RandomStringUtils.randomNumeric(16);
        String actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        assertThat(actual, is(equalTo("Should be 15 or 18 characters.")));
    }

    @Test
    @Description("Cloud reference should be disabled for on premise customers.")
    public void testCloudReferenceIsDisabledForOnPremiseCustomers() {

        boolean actual = customerProfilePage
            .selectCustomerTypeOnPremise()
            .isCloudReferenceEnabled();
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    @Description("Cloud reference should be enabled for cloud customers.")
    public void testCloudReferenceIsEnabledForCloudCustomers() {

        boolean actual = customerProfilePage
            .selectCustomerTypeCloud()
            .isCloudReferenceEnabled();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("Cloud reference should be enabled for cloud and on premise customers.")
    public void testCloudReferenceIsEnabledForCloudAndOnPremiseCustomers() {

        boolean actual = customerProfilePage
            .selectCustomerTypeOnPremiseAndCloud()
            .isCloudReferenceEnabled();
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    @Description("Cloud reference should be required if it is enabled.")
    public void testCloudReferenceIsRequiredIfEnabled() {

        String actual = customerProfilePage
            .selectCustomerTypeCloud()
            .enterCloudRef(null)
            .getCloudRefFeedback();
        assertThat(actual, is(equalTo("Enter a cloud reference.")));
    }

    @Test
    @Description("Cloud reference should not be required for on premise customers.")
    public void testCloudReferenceIsNotRequiredIfDisabled() {

        String actual = customerProfilePage
            .selectCustomerTypeCloud()
            .enterCloudRef("cloud-reference")
            .selectCustomerTypeOnPremise()
            .getCloudRefFeedback();
        assertThat(actual, is(equalTo("")));
    }

    @Test
    @Description("Email domains should be required.")
    public void testEmailDomainsRequired() {

        String actual = customerProfilePage
            .enterEmailDomains(null)
            .getEmailDomFeedback();
        assertThat(actual, is(equalTo("Enter email patterns using \",\" between items.")));
    }

    @Test
    @Description("Invalid emails should not be allowed.")
    public void testEmailDomainsRequireValidEmails() {

        String actual = customerProfilePage
            .enterEmailDomains("aa")
            .getEmailDomFeedback();
        assertThat(actual, is(equalTo("Email domains must be comma separated, contain no spaces, and must include the top level domain of at least 2 characters.")));
    }

    @Test
    @Description("CAD File Retention Policy is required.")
    public void testCadFileRetentionPolicyIsRequired() {

        String actual = customerProfilePage
            .enterCadFileRetentionPolicy(null)
            .getCadFileRetentionPolicyFeedback();
        assertThat(actual, is(equalTo("Enter the CAD retention policy.")));
    }

    @Test
    @Description("CAD File Retention Policy requires at least one day.")
    public void testCadFileRetentionPolicyRequiresAtLeastOneDay() {

        String actual = customerProfilePage
            .enterCadFileRetentionPolicy("0")
            .getCadFileRetentionPolicyFeedback();
        assertThat(actual, is(equalTo("The retention policy requires at least 1 day.")));
    }

    @Test
    @Description("CAD File Retention Policy has a maximum of 1095 days.")
    public void testCadFileRetentionPolicyHasAMaximumOf1095Days() {

        String actual = customerProfilePage
            .enterCadFileRetentionPolicy("1096")
            .getCadFileRetentionPolicyFeedback();
        assertThat(actual, is(equalTo("The retention policy allows at most 1095 days.")));
    }

    @Test
    @Description("Max Cad File Size is required.")
    public void testMaxCadFileSizeIsRequired() {

        String actual = customerProfilePage
            .enterMaxCadFileSize(null)
            .getMaxCadFileSizeFeedback();
        assertThat(actual, is(equalTo("Enter the max CAD file size.")));
    }

    @Test
    @Description("Max CAD File Size should be at least 10 MB.")
    public void testMaxCadFileSizeShouldBeAtLeast10Mb() {

        String actual = customerProfilePage
            .enterMaxCadFileSize("9")
            .getMaxCadFileSizeFeedback();
        assertThat(actual, is(equalTo("You will require at least 10 MB for a CAD file.")));
    }

    @Test
    @Description("Max CAD File Size should be at most 100 MB.")
    public void testMaxCadFileSizeShouldBeAtMost100MB() {

        String actual = customerProfilePage
            .enterMaxCadFileSize("101")
            .getMaxCadFileSizeFeedback();
        assertThat(actual, is(equalTo("The maximum CAD file size cannot exceed 100 MB.")));
    }
}
