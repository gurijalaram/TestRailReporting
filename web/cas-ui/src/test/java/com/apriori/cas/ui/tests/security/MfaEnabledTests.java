package com.apriori.cas.ui.tests.security;

import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cas.ui.pageobjects.security.SecurityPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MfaEnabledTests extends TestBaseUI {
    private SecurityPage securityPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private CustomerUtil customerUtil;
    private Customer customer;
    private String customerIdentity;
    private String customerName;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser().useTokenInRequests();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);

        String cloudRef = generateStringUtil.generateCloudReference();
        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String email = "\\S+@".concat(customerName);

        customer = customerUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();
        customerIdentity = customer.getIdentity();

        securityPage = new CasLoginPage(driver)
            .login(requestEntityUtil.getEmbeddedUser())
            .openCustomer(customerIdentity)
            .goToSecurityPage();
    }

    @AfterEach
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Description("Validates security tab for MFA enabled customer")
    @TestRail(id = {5572, 5573, 13281})
    public void testMfaEnabledSecurityPage() {
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(securityPage.getFieldName())
            .overridingErrorMessage("Expected fields name to be \"Authentication:\", \"MFA Authentication Type:\", \"Supported Devices:\"")
            .contains("Authentication:", "MFA Authentication Type:", "Supported Devices:");

        securityPage.clickResetMfaButton()
            .clickCancelConfirmReset()
            .clickResetMfaButton()
            .clickOkConfirmReset();

        soft.assertThat(securityPage.getSuccessTextMessage())
            .overridingErrorMessage("Expected successfully notification message is displayed")
            .isEqualTo((String.format("MFA Reset has now been requested for all %s users.", customerName)));

        soft.assertAll();
    }
}