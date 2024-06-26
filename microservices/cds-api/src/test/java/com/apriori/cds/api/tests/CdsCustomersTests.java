package com.apriori.cds.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Customers;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomersTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {3252})
    @Description("API returns a list of all the available customers in the CDS DB")
    public void getCustomers() {
        ResponseWrapper<Customers> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getMaxCadFileRetentionDays()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3298})
    @Description("Add API customers")
    public void addCustomerTest() {
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        String emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        soft.assertThat(customer.getResponseEntity().getName()).isEqualTo(customerName);
        soft.assertAll();
    }
}
