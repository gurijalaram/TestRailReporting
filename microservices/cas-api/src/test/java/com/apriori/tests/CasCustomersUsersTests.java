package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.entity.response.UpdateUser;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomersUsersTests extends TestUtil {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CasTestUtil casTestUtil = new CasTestUtil();
    private String url = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers/");

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
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

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        String usersEndpoint = url + customerIdentity + "/users/";

        ResponseWrapper<CustomerUser> user = casTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        ResponseWrapper<CustomerUsers> customerUsers = new CommonRequestUtil().getCommonRequest(usersEndpoint, true, CustomerUsers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(customerUsers.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerUsers.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String userIdentity = customerUsers.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String userUrl = usersEndpoint + userIdentity;

        ResponseWrapper<CustomerUser> singleUser = new CommonRequestUtil().getCommonRequest(userUrl, true, CustomerUser.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

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

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = casTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        String identity = user.getResponseEntity().getIdentity();
        String profileIdentity = user.getResponseEntity().getUserProfile().getIdentity();

        ResponseWrapper<UpdateUser> updatedUser = casTestUtil.updateUser(userName, customerName, identity, customerIdentity, profileIdentity);

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

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = casTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));

        String identity = user.getResponseEntity().getIdentity();
        String mfaUrl = url + customerIdentity + "/users/" + identity + "/reset-mfa";

        ResponseWrapper resetMfa = casTestUtil.resetMfa(mfaUrl);

        assertThat(resetMfa.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }
}
