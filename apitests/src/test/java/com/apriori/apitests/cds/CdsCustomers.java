package com.apriori.apitests.cds;

import static org.hamcrest.Matchers.hasItems;

import com.apriori.apibase.services.cds.objects.Customer;
import com.apriori.apibase.services.cds.objects.Customers;
import com.apriori.apibase.services.cds.objects.Users;
import com.apriori.apibase.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.dao.ServiceConnector;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomers extends CdsTestUtil {
    private String url;

    private String[] customerTypes = {"ON_PREMISE_ONLY", "CLOUD_ONLY", "ON_PREMISE_AND_CLOUD"};

    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
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
