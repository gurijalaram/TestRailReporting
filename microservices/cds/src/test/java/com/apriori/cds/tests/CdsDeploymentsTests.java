package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.User;
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

public class CdsDeploymentsTests extends CdsTestUtil {
    private String url;
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
    @TestRail(testCaseId = "3301")
    @Description("Add a deployment to a customer")
    public void addCustomerDeployment() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String deploymentsEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/deployments")));

        ResponseWrapper<User> user = addUser(deploymentsEndpoint, User.class, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users/".concat(userIdentity))));

        assertThat(user.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));
    }
}
