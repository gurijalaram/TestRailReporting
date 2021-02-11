package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.User;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsCustomerUsers extends CdsTestUtil {
    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (userIdentityEndpoint != null) {
            delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3293")
    @Description("Add a user to a customer")
    public void addCustomerUsers() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String usersEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users")));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s", String.format(customerIdentity.concat("/users/%s"), userIdentity)));

        assertThat(user.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));
    }

    @Test
    @TestRail(testCaseId = "3250")
    @Description("Add a user to a customer")
    public void getCustomerUsers() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        String usersEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users")));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        assertThat(user.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_CREATED)));
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Users> response = getCommonRequest(usersEndpoint, true, Users.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), CoreMatchers.is(equalTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = "3281")
    @Description("Add a user to a customer")
    public void getCustomerUserByIdentity() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String salesForceId = new GenerateStringUtil().generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        String usersEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users")));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        assertThat(user.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_CREATED)));
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        String userIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users/".concat(userIdentity))));

        ResponseWrapper<User> response = getCommonRequest(userIdentityEndpoint, true, User.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getIdentity(), is(equalTo(userIdentity)));
        assertThat(response.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));
    }
}
