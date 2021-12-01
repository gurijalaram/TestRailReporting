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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        String customerName = String.format("QA Automation %s", salesforceId);

        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName)
            .selectCustomerTypeOnPremiseAndCloud()
            .enterCloudRef(salesforceId)
            .clickSaveButton();

        created.add(customerViewPage.findExistingCustomerIdentity());
    }

    @After
    public void teardown() {
        created.forEach((identity) -> cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, identity));
    }

    private void enterValue(String name, String value) {
        switch (name) {
            case "name":
                customerProfilePage.enterCustomerName(value);
                break;
            case "description":
                customerProfilePage.enterDescription(value);
                break;
            case "salesforceId":
                customerProfilePage.enterSalesforceId(value);
                break;
            case "cloudReference":
                customerProfilePage.enterCloudRef(value);
                break;
            case "emailDomains":
                customerProfilePage.enterEmailDomains(value);
                break;
            case "maxCadFileRetentionDays":
                customerProfilePage.enterCadFileRetentionPolicy(value);
                break;
            case "maxCadFileSize":
                customerProfilePage.enterMaxCadFileSize(value);
                break;
            default:
        }
    }

    @FunctionalInterface
    interface AssertionFunction {
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

        if (name.equals("customerType")) {
            customerProfilePage.selectCustomerTypeCloud();
        } else {
            enterValue(name, valueMap.get(name));
        }

        assertion.assertThat(customerProfilePage.canSave())
            .overridingErrorMessage("Expected save button to be enabled with %s input changes (%s -> %s).",
                name, resetValue, valueMap.get(name))
            .isTrue();

        if (name.equals("customerType")) {
            customerProfilePage.selectCustomerTypeOnPremiseAndCloud();
        } else {
            enterValue(name, resetValue);
        }
    }

    private void assertButtonAvailable(SoftAssertions soft, String label) {
        List<WebElement> elements = driver.findElements(By.xpath(String.format("//button[.='%s']", label)));
        soft.assertThat(elements.size())
            .overridingErrorMessage(String.format("Could not find the %s button", label))
            .isGreaterThan(0);
    }

    @Test
    @Description("Test that customer details do not save when validation fails and cancels")
    @TestRail(testCaseId = {"10056, 10057, 10058, 10059, 10060, 10061"})
    public void testEditAndCancel() {
        SoftAssertions soft = new SoftAssertions();

        assertAllLeftFields(soft, this::assertNonEditable);
        soft.assertThat(customerProfilePage.clickEditButton())
            .overridingErrorMessage("Expected edit button to be displayed and clickable.")
            .isNotNull();

        assertAllLeftFields(soft, this::assertEditable);
        assertButtonAvailable(soft, "Save");
        assertButtonAvailable(soft, "Cancel");

        soft.assertThat(customerProfilePage.canSave())
            .overridingErrorMessage("Expected save button to be disabled with no changes.")
            .isFalse();

        String resetValue = customerProfilePage.getInputValue("salesforceId");
        customerProfilePage.enterSalesforceId("");
        soft.assertThat(customerProfilePage.canSave())
            .overridingErrorMessage("Expected save button to be disabled with validation errors.")
            .isFalse();
        customerProfilePage.enterSalesforceId(resetValue);

        customerProfilePage.enterCustomerName("aPriori Internal");
        customerProfilePage.clickSaveButton();
        soft.assertThat(customerProfilePage.getToastifyError())
            .overridingErrorMessage("Expected error toast to be displayed on duplicate customer name.")
            .isNotNull();

        customerProfilePage.clickCancelButton(CustomerWorkspacePage.class);
        assertAllLeftFields(soft, this::assertNonEditable);

        soft.assertAll();
    }

    @Test
    @Category({SmokeTest.class})
    @Description("Test that customer details can be edited and saved.")
    @TestRail(testCaseId = {"10060"})
    public void testEditAndSave() {
        SoftAssertions soft = new SoftAssertions();

        customerProfilePage = customerProfilePage.clickEditButton();

        assertAllLeftFields(soft, this::assertSaveChanges);

        String newDescription = "Test Description Change";
        customerProfilePage.enterDescription(newDescription);
        customerProfilePage.clickSaveButton();
        assertAllLeftFields(soft, this::assertNonEditable);
        String savedDescription = customerProfilePage.getLabel("description").getText();
        soft.assertThat(savedDescription)
            .overridingErrorMessage("Expected changed description to equal %s. Actual %s.", newDescription, savedDescription)
            .isEqualTo(newDescription);

        soft.assertAll();
    }
}
