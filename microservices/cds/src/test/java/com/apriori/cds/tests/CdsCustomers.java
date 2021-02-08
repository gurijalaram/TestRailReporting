package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.Customers;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateCustomers(response.getResponseEntity());
    }


    @Test
    @TestRail(testCaseId = "3278")
    @Description("API returns a customer's information based on the supplied identity")
    public void getCustomerById() {
        url = String.format(url,
            String.format("customers/%s", Constants.getCdsIdentityCustomer()));

        ResponseWrapper<Customer> response = getCommonRequest(url, true, Customer.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateCustomer(response.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = "3250")
    @Description("API returns a list of all available users for the customer")
    public void getCustomerUsers() {
        url = String.format(url,
            String.format("customers/%s/users", Constants.getCdsIdentityCustomer()));

        ResponseWrapper<Users> response = getCommonRequest(url, true, Users.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @Description("Add API customers")
    public void addCustomers() {
        url = String.format(url, "customers");

        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        RequestEntity requestEntity = RequestEntity.init(url, Customer.class)
            .setHeaders(headers)
            .setBody("customer",
                new Customer().setName("Moya1020")
                    .setDescription("Add new customers api test")
                    .setCustomerType("CLOUD_ONLY")
                    .setCreatedBy("#SYSTEM00000")
                    .setSalesforceId("AutomationSalesIds")
                    .setActive(true)
                    .setMfaRequired(false)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(1095)
                    .setEmailRegexPatterns(Arrays.asList("S+friths.com", "s+friths.co.uk")));

        ResponseWrapper<Customer> responseWrapper = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        assertThat(responseWrapper.getResponseEntity().getResponse().getName(), is(equalTo("Moya101")));
    }

    /*
     * Customer Validation
     */
    private void validateCustomers(Customers customersResponse) {
        Object[] customers = customersResponse.getResponse().getItems().toArray();
        Arrays.stream(customers)
            .forEach(this::validate);
    }

    private void validateCustomer(Customer customerResponse) {
        Customer customer = customerResponse.getResponse();
        validate(customer);
    }

    private void validate(Object customerObj) {
        Customer customer = (Customer) customerObj;
        Assert.assertTrue(customer.getIdentity().matches("^[a-zA-Z0-9]+$"));
        Assert.assertThat(Arrays.asList(customerTypes), hasItems(customer.getCustomerType()));
    }
}
