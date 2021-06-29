package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
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
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomersTests {
    private static String updatedEmailPattern;
    private static String customerIdentity;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private String url;
    private String customerIdentityEndpoint;

    @BeforeClass
    public static void setCustomerDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        updatedEmailPattern = "\\S+@".concat(generateStringUtil.generateCustomerName());

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
    }

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"3252"})
    @Description("API returns a list of all the available customers in the CDS DB")
    public void getCustomers() {
        url = String.format(url, "customers");

        ResponseWrapper<Customers> response = cdsTestUtil.getCommonRequest(url, Customers.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getMaxCadFileRetentionDays(), is(not(nullValue())));
    }

    @Test
    @TestRail(testCaseId = {"3298"})
    @Description("Add API customers")
    public void addCustomerTest() {
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
    }

    @Test
    @TestRail(testCaseId = {"3278"})
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Customer> response = cdsTestUtil.getCommonRequest(customerIdentityEndpoint, Customer.class);
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getResponse().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
    }

    @Test
    @TestRail(testCaseId = {"5957"})
    @Description("Get customer applications")
    public void getCustomersApplications() {
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String applicationsEndpoint = String.format(url, String.format("customers/%s/applications", customerIdentity));

        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(applicationsEndpoint, Applications.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5305"})
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(customerIdentityEndpoint, Customer.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                new Customer().setEmailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk")));

        ResponseWrapper<Customer> updatedEmail = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());

        assertThat(updatedEmail.getResponseEntity().getResponse().getEmailRegexPatterns(), hasItem(updatedEmailPattern + ".com"));
    }
}
