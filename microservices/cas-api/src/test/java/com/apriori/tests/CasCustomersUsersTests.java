package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.entity.response.UpdateUser;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomersUsersTests {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @TestRail(testCaseId = {"5661", "5662", "5663"})
    @Description("Add a user to a customer, return a list of users for the customer, get the User identified by its identity.")
    public void addCustomerUsers() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        ResponseWrapper<CustomerUsers> customerUsers = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMERS, CustomerUsers.class)
            .token(token)
            .inlineVariables(customerIdentity, "users")).get();

        assertThat(customerUsers.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerUsers.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String userIdentity = customerUsers.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<CustomerUser> singleUser = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_USERS, CustomerUser.class)
            .token(token)
            .inlineVariables(customerIdentity, userIdentity)).get();

        assertThat(singleUser.getResponseEntity().getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5664"})
    @Description("Update the User.")
    public void updateCustomerUsers() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        String identity = user.getResponseEntity().getIdentity();
        String profileIdentity = user.getResponseEntity().getUserProfile().getIdentity();

        ResponseWrapper<UpdateUser> updatedUser = CasTestUtil.updateUser(userName, customerName, identity, customerIdentity, profileIdentity);

        assertThat(updatedUser.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment(), is(equalTo("QA")));
    }

    @Test
    @TestRail(testCaseId = {"5667"})
    @Description("Reset the MFA configuration for a user.")
    public void resettingUserMfa() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        String identity = user.getResponseEntity().getIdentity();

        ResponseWrapper<String> resetMfa = CasTestUtil.resetMfa(customerIdentity, identity);

        assertThat(resetMfa.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }
}
