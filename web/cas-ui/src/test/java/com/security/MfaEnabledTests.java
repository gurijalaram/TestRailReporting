package com.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.login.CasLoginPage;
import com.apriori.security.SecurityPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MfaEnabledTests extends TestBase {
    private SecurityPage securityPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private Customer customer;
    private String customerIdentity;
    private String customerName;

    @Before
    public void setup() {
        String cloudRef = generateStringUtil.generateCloudReference();
        customerName = generateStringUtil.generateCustomerName();
        String email = "\\S+@".concat(customerName);

        cdsTestUtil = new CdsTestUtil();
        customer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();
        customerIdentity = customer.getIdentity();

        securityPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToSecurityPage();
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Description("Validates security tab for MFA enabled customer")
    @TestRail(testCaseId = {"5572", "5573", "13281"})
    public void testMfaEnabledSecurityPage() {
        assertThat(securityPage.getFieldName(), containsInRelativeOrder("Authentication:", "MFA Authentication Type:", "Supported Devices:"));

        securityPage.clickResetMfaButton()
            .clickCancelConfirmReset()
            .clickResetMfaButton()
            .clickOkConfirmReset();

        assertThat(securityPage.getSuccessTextMessage(), is(equalTo(String.format("MFA Reset has now been requested for all %s users.", customerName))));
    }
}