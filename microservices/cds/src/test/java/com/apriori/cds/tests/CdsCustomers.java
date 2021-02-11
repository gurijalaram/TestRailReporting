package com.apriori.cds.tests;

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
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomers extends CdsTestUtil {
    private String url;

    private String[] customerTypes = {"ON_PREMISE_ONLY", "CLOUD_ONLY", "ON_PREMISE_AND_CLOUD"};

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = "3252")
    @Description("API returns a list of all the available customers in the CDS DB")
    public void getCustomers() {
        url = String.format(url, "customers");

        ResponseWrapper<Customers> response = getCommonRequest(url, true, Customers.class);

        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), CoreMatchers.is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getMaxCadFileRetentionDays(), CoreMatchers.is(not(nullValue())));
    }

    @Test
    @Description("Add API customers")
    public void addCustomerTest() {
        url = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(url, Customer.class, customerName, cloudRef, salesForceId, emailPattern);

        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
    }

    @Test
    @Description("Get customer by Identity")
    public void getCustomerByIdentity() {
        String customersUrl = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customersUrl, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        String customerIdentityUrl = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Customer> response = getCommonRequest(customerIdentityUrl, true, Customer.class);
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
        assertThat(response.getResponseEntity().getResponse().getEmailRegexPatterns(), is(Arrays.asList(emailPattern + ".com", emailPattern + ".co.uk")));
    }

    @Test
    @Description("Get customer by Identity")
    public void getCustomersApplications() {
        String customersUrl = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customersUrl, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        String applicationsUrl = String.format(url, String.format("customers/%s", customerIdentity.concat("/applications")));

        ResponseWrapper<Applications> response = getCommonRequest(applicationsUrl, true, Applications.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), CoreMatchers.is(equalTo(0)));
    }
}
