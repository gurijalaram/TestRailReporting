package com.apriori.cds.tests;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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

    @Before
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

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3278"})
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        ResponseWrapper<Customer> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(response.getResponseEntity().getName()).isEqualTo(customerName);
        soft.assertThat(response.getResponseEntity().getEmailRegexPatterns()).isEqualTo(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk"));
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5957"})
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
    @TestRail(testCaseId = {"5305"})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        ResponseWrapper<Customer> updatedEmail = cdsTestUtil.updateCustomer(customerIdentity, updatedEmailPattern);

        soft.assertThat(updatedEmail.getResponseEntity().getEmailRegexPatterns()).contains(updatedEmailPattern + ".com");
        soft.assertAll();
    }
}
