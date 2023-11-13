package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Applications;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

@ExtendWith(TestRulesAPI.class)
public class CdsGetCustomerTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String updatedEmailPattern;

    @BeforeEach
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        updatedEmailPattern = "\\S+@".concat(generateStringUtil.generateCustomerName());
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {3278})
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        ResponseWrapper<Customer> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(response.getResponseEntity().getName()).isEqualTo(customerName);
        soft.assertThat(response.getResponseEntity().getEmailRegexPatterns()).isEqualTo(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk"));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5957})
    @Description("Get customer applications")
    public void getCustomersApplications() {
        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_APPLICATION_BY_CUSTOMER_ID,
            Applications.class,
            HttpStatus.SC_OK,
            customerIdentity
        );
        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isEqualTo(0);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5305})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        ResponseWrapper<Customer> updatedEmail = cdsTestUtil.updateCustomer(customerIdentity, updatedEmailPattern);

        soft.assertThat(updatedEmail.getResponseEntity().getEmailRegexPatterns()).contains(updatedEmailPattern + ".com");
        soft.assertAll();
    }
}