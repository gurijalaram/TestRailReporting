package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Customers;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdsCustomersTests {
    private String url;
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @BeforeEach
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
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
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        soft.assertThat(customer.getResponseEntity().getName()).isEqualTo(customerName);
        soft.assertAll();
    }
}
