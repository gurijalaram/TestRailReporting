package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.Applications;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.Customers;
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
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomersTests extends CdsTestUtil {
    private String url;

    private String customerIdentity;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3252")
    @Description("API returns a list of all the available customers in the CDS DB")
    public void getCustomers() {
        url = String.format(url, "customers");

        ResponseWrapper<Customers> response = getCommonRequest(url, true, Customers.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getMaxCadFileRetentionDays(), is(not(nullValue())));
    }

    @Test
    @Description("Add API customers")
    public void addCustomerTest() {
        String customerEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customerEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
    }

    @Test
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Customer> response = getCommonRequest(customerIdentityEndpoint, true, Customer.class);
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getResponse().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
    }

    @Test
    @Description("Get customer by Identity")
    public void getCustomersApplications() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String applicationsEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/applications")));

        ResponseWrapper<Applications> response = getCommonRequest(applicationsEndpoint, true, Applications.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), CoreMatchers.is(equalTo(0)));
    }

    @Test
    @Description("Update customer info by id")
    public void updateCustomerInfoId() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String updatedEmailPattern = "\\S+@".concat(generateStringUtil.generateCustomerName());

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(customerIdentityEndpoint, Customer.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                new Customer().setEmailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk")));

        ResponseWrapper<Customer> updatedEmail = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());

        assertThat(updatedEmail.getResponseEntity().getResponse().getEmailRegexPatterns(), hasItem(updatedEmailPattern + ".com"));
    }
}