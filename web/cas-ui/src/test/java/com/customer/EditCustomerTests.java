package com.customer;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
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
    private String cloudReference = new GenerateStringUtil().generateCloudReference();

    @Before
    public void setup() {
        created = new ArrayList<>();
        valueMap = new HashMap<>();
        valueMap.put("name", "Set Name Test");
        valueMap.put("description", "Set Description Test");
        valueMap.put("customerType", "Cloud");
        valueMap.put("salesforceId", "200000000000002");
        valueMap.put("cloudReference", cloudReference);
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
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String customerName = String.format("QA Automation %s", salesforceId);

        customerProfilePage
            .enterDescription("Automation Test Customer")
            .enterSalesforceId(salesforceId)
            .enterEmailDomains("apriori.com")
            .enterCustomerName(customerName)
            .selectCustomerTypeOnPremiseAndCloud()
            .enterCloudRef(cloudRef)
            .clickSaveButton()
            .clickEditButton()
            .clickCancelButton(CustomerProfilePage.class);

        created.add(customerViewPage.findCustomerIdentity());
    }

    @After
    public void teardown() {
        if (created != null) {
            created.forEach(identity -> cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, identity));
        }
    }

    @FunctionalInterface
    interface AssertionFunction {
        void apply(SoftAssertions assertion, Runnable setValueFn, String name);
    }

    private void assertAllLeftFields(SoftAssertions soft, AssertionFunction func) {
        func.apply(soft, () -> customerProfilePage.enterCustomerName(valueMap.get("name")), "name");
        func.apply(soft, () -> customerProfilePage.enterDescription(valueMap.get("description")), "description");
        func.apply(soft, () -> customerProfilePage.selectCustomerTypeCloud(), "customerType");
        func.apply(soft, () -> customerProfilePage.enterSalesforceId(valueMap.get("salesforceId")), "salesforceId");
        func.apply(soft, () -> customerProfilePage.enterCloudRef(valueMap.get("cloudReference")),"cloudReference");
        func.apply(soft, () -> customerProfilePage.enterEmailDomains(valueMap.get("emailDomains")),"emailDomains");
        func.apply(soft, () -> customerProfilePage.enterCadFileRetentionPolicy(valueMap.get("maxCadFileRetentionDays")),"maxCadFileRetentionDays");
        func.apply(soft, () -> customerProfilePage.enterMaxCadFileSize(valueMap.get("maxCadFileSize")),"maxCadFileSize");
    }

    private void assertNonEditable(SoftAssertions assertion, Runnable action, String name) {
        assertion.assertThat(customerProfilePage.getReadOnlyLabel(name))
            .overridingErrorMessage("Expected field of %s to be read only.", name)
            .isNotNull();
    }

    private void assertEditable(SoftAssertions assertion, Runnable action, String name) {
        assertion.assertThat(customerProfilePage.getInput(name))
            .overridingErrorMessage("Expected field of %s to be editable.", name)
            .isNotNull();
    }

    private void assertSaveChanges(SoftAssertions assertion, Runnable action, String name) {
        action.run();

        assertion.assertThat(customerProfilePage.canSave())
            .overridingErrorMessage("Expected save button to be enabled when %s changed.", name)
            .isTrue();

        customerProfilePage.clickCancelButton(CustomerWorkspacePage.class);
        customerProfilePage.clickEditButton();
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
        String savedDescription = customerProfilePage.getReadOnlyLabel("description").getText();
        soft.assertThat(savedDescription)
            .overridingErrorMessage("Expected changed description to equal %s. Actual %s.", newDescription, savedDescription)
            .isEqualTo(newDescription);

        soft.assertAll();
    }
}
