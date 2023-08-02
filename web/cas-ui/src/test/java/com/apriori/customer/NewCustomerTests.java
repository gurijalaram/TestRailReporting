package com.apriori.customer;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;

import com.apriori.PageUtils;
import com.apriori.TestBaseUI;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.components.SourceListComponent;
import com.apriori.http.utils.Obligation;
import com.apriori.pageobjects.customer.CustomerWorkspacePage;
import com.apriori.pageobjects.customeradmin.CustomerAdminPage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.pageobjects.newcustomer.CustomerProfilePage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewCustomerTests extends TestBaseUI {
    private CustomerWorkspacePage customerViewPage;
    private CustomerProfilePage customerProfilePage;
    private CdsTestUtil cdsTestUtil;
    private List<String> created;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setup() {
        created = new ArrayList<>();
        cdsTestUtil = new CdsTestUtil();
        customerViewPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .clickNewCustomerButton();
        customerProfilePage = customerViewPage.goToProfile();
    }

    @AfterEach
    public void teardown() {
        if (created != null) {
            created.forEach(identity -> cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, identity));
        }
    }

    @Test
    @Description("Clicking the New Customer button takes me to the profile form and all displays are valid.")
    @TestRail(id = {9600})
    public void testValidateThatTheFormLabelsAreCorrect() {
        soft.assertThat(customerViewPage.getProfileTab().isActive())
            .overridingErrorMessage("The profile tab not the active tab.")
            .isTrue();
        soft.assertThat(customerViewPage.getUsersTab().isEnabled())
            .overridingErrorMessage("The users tab is enabled on a new customer.")
            .isFalse();
        soft.assertThat(customerViewPage.getInfrastructureTab().isEnabled())
            .overridingErrorMessage("The infrastructure tab is enabled on a new customer.")
            .isFalse();
        soft.assertThat(customerViewPage.getSecurityTab())
            .overridingErrorMessage("The security tab is visible on a new customer.")
            .isNull();
        soft.assertThat(customerViewPage.getSystemConfigurationTab())
            .overridingErrorMessage("The system configuration tab is enabled on a new customer.")
            .isNull();

        List<String> labels = Arrays.asList(
            "Customer Name:",
            "Description:",
            "Customer Type:",
            "Salesforce ID:",
            "Cloud Reference:",
            "Email Domains:",
            "CAD File Retention Policy (days):",
            "Max CAD File Size:",
            "Last Updated:",
            "Updated By:",
            "Created:",
            "Created By:",
            "Authentication:"
        );

        testNewCustomerLabelAvailable(labels, soft);
        soft.assertAll();
    }

    private void testNewCustomerLabelAvailable(List<String> labels, SoftAssertions soft) {
        labels.forEach(label -> {
            List<WebElement> elements = driver.findElements(By.xpath(String.format("//label[.='%s']", label)));
            soft.assertThat(elements.size())
                .overridingErrorMessage(String.format("Could not find the label, %s", label))
                .isGreaterThan(0);
        });
    }

    @Test
    @Tag(SMOKE)
    @Description("Clicking the cancel button returns the user to the customer list.")
    @TestRail(id = {9613})
    public void testCancelReturnsTheUserToTheCustomerListWhenCreatingANewCustomer() {

        CustomerAdminPage actual = customerProfilePage.clickCancelButton(CustomerAdminPage.class);
        soft.assertThat(actual).isNotNull();
        soft.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("The save button creates the new customer and reloads the page in edit mode.")
    @TestRail(id = {9616, 10627})
    public void testSaveCreatesTheNewCustomerAndThePageReloadsWithTheNewCustomer() {

        DateFormat format = new SimpleDateFormat("0yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String salesforceId = format.format(new Date());
        String customerName = String.format("QA Automation %s", salesforceId);
        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName);

        soft.assertThat(customerProfilePage.isSaveButtonEnabled()).isTrue();
        customerProfilePage.enterCustomerName(null);
        soft.assertThat(customerProfilePage.isSaveButtonEnabled()).isFalse();

        CustomerProfilePage editPage = customerProfilePage
            .enterCustomerName(customerName)
            .clickSaveButton()
            .clickEditButton();

        created.add(customerViewPage.findCustomerIdentity());

        soft.assertThat(editPage).isNotNull();

        editPage.clickCancelButton(CustomerProfilePage.class);

        soft.assertThat(customerProfilePage.getCustomerName()).isEqualTo(customerName);
        soft.assertAll();
    }

    private void testTheNecessaryFieldsAreRequired() {

        customerProfilePage.enterCustomerName(null);
        soft.assertThat(customerProfilePage.getCustomerNameFeedback())
            .isEqualTo("Enter a customer name. This should be the company's official name or the name in Salesforce.");
        customerProfilePage.enterDescription(null);
        soft.assertThat(customerProfilePage.getDescriptionFeedback())
            .isEqualTo("Enter a description for the customer.");
        customerProfilePage.clearCustomerType();
        soft.assertThat(customerProfilePage.getCustomerTypeFeedback())
            .isEqualTo("Select the customer type.");
        customerProfilePage.enterSalesforceId(null);
        soft.assertThat(customerProfilePage.getSalesforceIdFeedback())
            .isEqualTo("Please enter the salesforce id.");
        customerProfilePage.enterEmailDomains(null);
        soft.assertThat(customerProfilePage.getEmailDomFeedback())
            .isEqualTo("Enter email patterns using \",\" between items.");
        customerProfilePage.enterCadFileRetentionPolicy(null);
        soft.assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback())
            .isEqualTo("Enter the CAD retention policy.");
        customerProfilePage.enterMaxCadFileSize(null);
        soft.assertThat(customerProfilePage.getMaxCadFileSizeFeedback())
            .isEqualTo("Enter the max CAD file size.");
    }

    private void testCustomerNameShouldBeNoMoreThan64Characters() {

        String nameTooLong = RandomStringUtils.randomAlphanumeric(65);
        String actual = customerProfilePage
            .enterCustomerName(nameTooLong)
            .getCustomerNameFeedback();
        soft.assertThat(actual).isEqualTo("Should be no more than 64 characters in length.");
    }

    private void testSalesforceIdShouldBe15Or18Characters(int count) {

        String salesforceId = RandomStringUtils.randomNumeric(count);
        String actual = customerProfilePage
            .enterSalesforceId(salesforceId)
            .getSalesforceIdFeedback();
        soft.assertThat(actual).isEqualTo("Should be 15 or 18 characters.");
    }

    private void testEmailDomainsRequireValidEmails() {

        customerProfilePage.enterEmailDomains("aa");
        soft.assertThat(customerProfilePage.getEmailDomFeedback()).isEqualTo("Email domains must be comma separated, contain no spaces, and must include the top level domain of at least 2 characters.");
    }

    private void testCadFileRetentionPolicyRequiresAtLeastOneDayAndAtMost1095Days() {

        customerProfilePage.enterCadFileRetentionPolicy("0");
        soft.assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback()).isEqualTo("The retention policy requires at least 1 day.");
        customerProfilePage.enterCadFileRetentionPolicy("1096");
        soft.assertThat(customerProfilePage.getCadFileRetentionPolicyFeedback()).isEqualTo("The retention policy allows at most 1095 days.");
    }

    private void testMaxCadFileSizeShouldBeAtLeast10MbAndAtMost100MB() {

        customerProfilePage.enterMaxCadFileSize("9");
        soft.assertThat(customerProfilePage.getMaxCadFileSizeFeedback()).isEqualTo("You will require at least 10 MB for a CAD file.");
        customerProfilePage.enterMaxCadFileSize("101");
        soft.assertThat(customerProfilePage.getMaxCadFileSizeFeedback()).isEqualTo("The maximum CAD file size cannot exceed 100 MB.");
    }

    @Test
    @Description("All expected validations show up.")
    @TestRail(id = {9617, 9618, 9622, 9629, 9631, 9634})
    public void testRequiredValidations() {

        testTheNecessaryFieldsAreRequired();
        testCustomerNameShouldBeNoMoreThan64Characters();
        testSalesforceIdShouldBe15Or18Characters(14);
        testSalesforceIdShouldBe15Or18Characters(16);
        testSalesforceIdShouldBe15Or18Characters(17);
        testSalesforceIdShouldBe15Or18Characters(19);
        testEmailDomainsRequireValidEmails();
        testCadFileRetentionPolicyRequiresAtLeastOneDayAndAtMost1095Days();
        testMaxCadFileSizeShouldBeAtLeast10MbAndAtMost100MB();
        soft.assertAll();
    }

    @Test
    @Description("Cloud reference should be disabled for on premise customers and required for cloud customers.")
    @TestRail(id = {9623})
    public void testCloudReferenceIsDisabledForOnPremiseCustomers() {

        customerProfilePage.selectCustomerTypeOnPremise();
        soft.assertThat(customerProfilePage.isCloudReferenceEnabled()).isFalse();
        customerProfilePage.selectCustomerTypeCloud();
        soft.assertThat(customerProfilePage.isCloudReferenceEnabled()).isTrue();
        customerProfilePage.selectCustomerTypeOnPremiseAndCloud();
        soft.assertThat(customerProfilePage.isCloudReferenceEnabled()).isTrue();
        customerProfilePage.enterCloudRef(null);
        soft.assertThat(customerProfilePage.getCloudRefFeedback()).isEqualTo("Enter a cloud reference.");
        customerProfilePage.selectCustomerTypeOnPremise();
        soft.assertThat(customerProfilePage.getCloudRefFeedback()).isEqualTo("");
        soft.assertThat(customerProfilePage.getCloudRefValue()).isEqualTo("");
        soft.assertThat(customerProfilePage.isCloudReferenceEnabled()).isFalse();
        soft.assertAll();
    }

    @Test
    @Disabled("Status is currently disabled in customer profile")
    @Description("Validate that customer can be set to inactive by unselecting Status checkbox")
    @TestRail(id = {10633})
    public void testNewCustomerCanBeCreatedWithInactiveStatus() {
        DateFormat format = new SimpleDateFormat("0yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String salesforceId = format.format(new Date());
        String customerName = String.format("QA Automation %s", salesforceId);
        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName)
            .changeCustomerStatus()
            .clickSaveButton();

        soft.assertThat(customerProfilePage.getStatus()).isEqualTo("Inactive");

        String customerIdentity = customerViewPage.findCustomerIdentity();

        CustomerAdminPage findCustomer = customerViewPage.goToCustomersList()
            .clickCardViewButton();

        PageUtils utils = new PageUtils(driver);

        SourceListComponent customers = findCustomer.getSourceList();
        Obligation.mandatory(customers::getSearch, "Customers list search is missing").search(customerIdentity);
        utils.waitForCondition(customers::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(findCustomer.isStatusIconColour("red")).isTrue();
        soft.assertAll();
    }
}
