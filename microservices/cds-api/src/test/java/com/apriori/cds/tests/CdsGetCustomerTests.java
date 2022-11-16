package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class CdsGetCustomerTests {

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
        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
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
        assertThat(response.getResponseEntity().getTotalItemCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5305"})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class)
            .inlineVariables(customerIdentity)
            .headers(new HashMap<String, String>() {{
                    put("Content-Type", "application/json");
                }
            })
            .body("customer",
                Customer.builder()
                    .emailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk"))
                    .build());

        ResponseWrapper<Customer> updatedEmail = HTTPRequest.build(requestEntity).patch();

        assertThat(updatedEmail.getResponseEntity().getEmailRegexPatterns(), hasItem(updatedEmailPattern + ".com"));
    }
}
