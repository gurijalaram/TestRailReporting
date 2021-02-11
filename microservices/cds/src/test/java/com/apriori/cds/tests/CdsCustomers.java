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
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomers extends CdsTestUtil {
    private String url;

    private String[] customerTypes = {"ON_PREMISE_ONLY", "CLOUD_ONLY", "ON_PREMISE_AND_CLOUD"};
    private String customerIdentity;
    private String deleteCustomerEndpoint;
    private String deleteUserEndpoint;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (deleteCustomerEndpoint != null) {
            deleteCustomer(deleteCustomerEndpoint);
        }
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
    @Description("Add API customer")
    public void addCustomer() {
        String customerEndpoint = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "S+@".concat(customerName);

        ResponseWrapper<Customer> customer = addCustomer(customerEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        deleteCustomerEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));
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
