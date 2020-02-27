package com.apriori.internalapi.services;

import static org.hamcrest.Matchers.hasItems;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.objects.Customer;
import com.apriori.apibase.services.objects.Customers;
import com.apriori.apibase.services.objects.Users;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsCustomers {
    private String url;
    
    private String[] customerTypes = {"ON_PREMISE_ONLY","CLOUD_ONLY","ON_PREMISE_AND_CLOUD"};
    
    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
    }
    
    @Test
    @TestRail(testCaseId = "3252")
    @Description("API returns a list of all the available customers in the CDS DB")
    public void getCustomers() {
        url = String.format(url, "customers"); 
        Customers response = (Customers)ServiceConnector.getService(url, Customers.class); 
        validateCustomers(response);
    } 

    @Test
    @TestRail(testCaseId = "3278")
    @Description("API returns a customer's information based on the supplied identity")
    public void getCustomerById() {
        url = String.format(url, 
            String.format("customers/%s", Constants.getCdsIdentityCustomer())); 
        Customer response = (Customer)ServiceConnector.getService(url, Customer.class); 
        validateCustomer(response);
    }
    
    @Test
    @TestRail(testCaseId = "3250")
    @Description("API returns a list of all available users for the customer")
    public void getCustomerUsers() {
        url = String.format(url, 
            String.format("customers/%s/users", Constants.getCdsIdentityCustomer())); 
        ServiceConnector.getService(url, Users.class);  
    }
    
    /*
     * Customer Validation
     */
    private void validateCustomers(Customers customersResponse) {
        Object[] customers = customersResponse.getResponse().getItems().toArray();
        Arrays.stream(customers)
            .forEach(c -> validate(c));
    }
    
    private void validateCustomer(Customer customerResponse) {
        Customer customer = customerResponse.getResponse();
        validate(customer);
    }
    
    private void validate(Object customerObj) {
        Customer customer = (Customer)customerObj;
        Assert.assertTrue(customer.getIdentity().matches("^[a-zA-Z0-9]+$"));
        Assert.assertThat(Arrays.asList(customerTypes), hasItems(customer.getCustomerType()));
    }
}
