package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class CdsGetCustomerTests {

    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String url;
    private static String customerIdentityEndpoint;
    private static String updatedEmailPattern;

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        updatedEmailPattern = "\\S+@".concat(generateStringUtil.generateCustomerName());

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"3278"})
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        assertThat(customer.getResponseEntity().getName(), is(equalTo(customerName)));

        ResponseWrapper<Customer> response = cdsTestUtil.getCommonRequest(customerIdentityEndpoint, Customer.class);
        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
    }

    @Test
    @TestRail(testCaseId = {"5957"})
    @Description("Get customer applications")
    public void getCustomersApplications() {
        assertThat(customer.getResponseEntity().getName(), is(equalTo(customerName)));

        String applicationsEndpoint = String.format(url, String.format("customers/%s/applications", customerIdentity));

        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(applicationsEndpoint, Applications.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5305"})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        assertThat(customer.getResponseEntity().getName(), is(equalTo(customerName)));

        RequestEntity requestEntity = RequestEntity.init(customerIdentityEndpoint, Customer.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                Customer.builder()
                    .emailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk"))
                    .build());

        ResponseWrapper<Customer> updatedEmail = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());

        assertThat(updatedEmail.getResponseEntity().getEmailRegexPatterns(), hasItem(updatedEmailPattern + ".com"));
    }
}
