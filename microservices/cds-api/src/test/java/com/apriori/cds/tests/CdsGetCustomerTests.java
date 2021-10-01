package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class CdsGetCustomerTests {

    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String updatedEmailPattern;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        updatedEmailPattern = "\\S+@".concat(generateStringUtil.generateCustomerName());

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3278"})
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        ResponseWrapper<Customer> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMER_BY_ID, Customer.class, customerIdentity);
        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
    }

    @Test
    @TestRail(testCaseId = {"5957"})
    @Description("Get customer applications")
    public void getCustomersApplications() {
        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMERS_APPLICATION_BY_CUSTOMER_ID,
            Applications.class,
            customerIdentity
        );
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5305"})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.PATCH_CUSTOMERS_BY_ID, Customer.class)
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
