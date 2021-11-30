package com.customer;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class EditCustomerTests extends TestBase {

    private CustomerProfilePage customerProfilePage;
    private CdsTestUtil cdsTestUtil;
    private List<String> created;
    private Map<String, String> valueMap;
    private String customerName;

    @Before
    public void setup() {
        created = new ArrayList<>();
        valueMap = new HashMap<>();
        valueMap.put("name", "Set Name Test");
        valueMap.put("description", "Set Description Test");
        valueMap.put("customerType", "Cloud");
        valueMap.put("salesforceId", "200000000000002");
        valueMap.put("cloudReference", "200000000000002");
        valueMap.put("emailDomains", "testCustomer.com");
        valueMap.put("maxCadFileRetentionDays", "1");
        valueMap.put("maxCadFileSize", "10");

        cdsTestUtil = new CdsTestUtil();
        CustomerWorkspacePage customerViewPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .clickNewCustomerButton();
        customerProfilePage = customerViewPage.goToProfile();

        DateFormat format = new SimpleDateFormat("0yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String salesforceId = format.format(new Date());
        customerName = String.format("QA Automation %s", salesforceId);

        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName)
            .selectCustomerTypeOnPremiseAndCloud()
            .enterCloudRef(salesforceId)
            .clickSaveButton();

        created.add(customerViewPage.findCustomerIdentity());
    }

    @After
    public void teardown() {
        created.forEach((identity) -> cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, identity));
    }

    @FunctionalInterface
    interface AssertionFunction<SoftAssertions, String> {
        void apply(SoftAssertions assertion, String name);
    }

    private void assertAllLeftFields(SoftAssertions soft, AssertionFunction func) {
        func.apply(soft, "name");
        func.apply(soft, "description");
        func.apply(soft, "customerType");
        func.apply(soft, "salesforceId");
        func.apply(soft, "cloudReference");
        func.apply(soft, "emailDomains");
        func.apply(soft, "maxCadFileRetentionDays");
        func.apply(soft, "maxCadFileSize");
    }

    private void assertNonEditable(SoftAssertions assertion, String name) {
        assertion.assertThat(customerProfilePage.getLabel(name))
            .overridingErrorMessage("Expected field of %s to be read only.", name)
            .isNotNull();
    }

    private void assertEditable(SoftAssertions assertion, String name) {
        assertion.assertThat(customerProfilePage.getInput(name))
            .overridingErrorMessage("Expected field of %s to be editable.", name)
            .isNotNull();
    }

    private void assertSaveChanges(SoftAssertions assertion, String name) {
        String resetValue = customerProfilePage.getInputValue(name);

        // Set value
        customerProfilePage.enterInputValuePair(name, valueMap.get(name));

        assertion.assertThat(customerProfilePage.getSaveButton().getAttribute("class").contains("disabled"))
            .overridingErrorMessage("Expected save button to be enabled with %s input changes (%s -> %s).",
                name, resetValue, valueMap.get(name))
            .isFalse();

        // Set reset
        customerProfilePage.enterInputValuePair(name, resetValue);
    }

    @Test
    @Description("Test that customer details do not save when validation fails and cancels")
    @TestRail(testCaseId = {"10056, 10057, 10058, 10059, 10060, 10061"})
    public void testEditAndCancel() {
        SoftAssertions soft = new SoftAssertions();

        // Test customer profile page load -> profile not editable & edit button displayed
        assertAllLeftFields(soft, (AssertionFunction<SoftAssertions, String>) this::assertNonEditable);
        soft.assertThat(customerProfilePage.clickEditButton())
            .overridingErrorMessage("Expected edit button to be displayed and clickable.")
            .isNotNull();

        // Test edit button clicked -> save and cancel buttons become visible && left side fields editable
        assertAllLeftFields(soft, (AssertionFunction<SoftAssertions, String>) this::assertEditable);
        soft.assertThat(customerProfilePage.getSaveButton())
            .overridingErrorMessage("Expected save button to be displayed.")
            .isNotNull();
        soft.assertThat(customerProfilePage.getCancelButton())
            .overridingErrorMessage("Expected cancel button to be displayed.")
            .isNotNull();

        // Test no changes on form -> Save button disabled
        soft.assertThat(customerProfilePage.getSaveButton().getAttribute("class").contains("disabled"))
            .overridingErrorMessage("Expected save button to be disabled with no changes.")
            .isTrue();

        // Test validation errors on form -> Save button disabled
        String resetValue = customerProfilePage.getInputValue("salesforceId");
        customerProfilePage.enterSalesforceId("");
        soft.assertThat(customerProfilePage.getSaveButton().getAttribute("class").contains("disabled"))
            .overridingErrorMessage("Expected save button to be disabled with validation errors.")
            .isTrue();
        customerProfilePage.enterSalesforceId(resetValue);

        // Test save button clicked with error -> Error toast displayed
        customerProfilePage.enterCustomerName("aPriori Internal");
        customerProfilePage.clickSaveButton();
        soft.assertThat(customerProfilePage.getToastifyError())
            .overridingErrorMessage("Expected error toast to be displayed on duplicate customer name.")
            .isNotNull();

        // Test cancel button clicked -> Form is reset to non editable state
        customerProfilePage.clickCancelButton(CustomerWorkspacePage.class);
        assertAllLeftFields(soft, (AssertionFunction<SoftAssertions, String>) this::assertNonEditable);

        soft.assertAll();
    }

    @Test
    @Category({SmokeTest.class})
    @Description("Test that customer details can be edited and saved.")
    @TestRail(testCaseId = {"10060"})
    public void testEditAndSave() {
        SoftAssertions soft = new SoftAssertions();

        customerProfilePage = customerProfilePage.clickEditButton();

        // Test changes made to fields -> Save button enabled
        assertAllLeftFields(soft, (AssertionFunction<SoftAssertions, String>) this::assertSaveChanges);

        // Test save button clicked with no error -> Record saved and returned to readonly
        String newDescription = "Test Description Change";
        customerProfilePage.enterDescription(newDescription);
        customerProfilePage.clickSaveButton();
        assertAllLeftFields(soft, (AssertionFunction<SoftAssertions, String>) this::assertNonEditable);
        String savedDescription = customerProfilePage.getLabel("description").getText();
        soft.assertThat(savedDescription)
            .overridingErrorMessage("Expected changed description to equal %s. Actual %s.", newDescription, savedDescription)
            .isEqualTo(newDescription);

        soft.assertAll();
    }
}
