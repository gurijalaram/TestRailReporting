package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.SingleCustomer;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.User;
import com.apriori.entity.response.Users;

import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomersUsersTests extends TestUtil {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCasServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCasTokenUsername(),
                Constants.getCasTokenEmail(),
                Constants.getCasTokenIssuer(),
                Constants.getCasTokenSubject());
    }

    @Test
    @TestRail(testCaseId = {"5661", "5662", "5663"})
    @Description("Add a user to a customer, return a list of users for the customer, get the User identified by its identity.")
    public void addCustomerUsers() {
        String url = String.format(Constants.getApiUrl(), "customers/");
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<SingleCustomer> customer = new CasTestUtil().addCustomer(url, SingleCustomer.class, token, customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        String usersEndpoint = url + customerIdentity + "/users/";

        ResponseWrapper<User> user = new CasTestUtil().addUser(usersEndpoint, User.class, token, userName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));

        ResponseWrapper<Users> customerUsers = new CommonRequestUtil().getCommonRequest(usersEndpoint, true, Users.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(customerUsers.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerUsers.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String userIdentity = customerUsers.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String userUrl = usersEndpoint + userIdentity;

        ResponseWrapper<User> singleUser = new CommonRequestUtil().getCommonRequest(userUrl, true, User.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(singleUser.getResponseEntity().getResponse().getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = "5664")
    @Description("Update the User.")
    public void updateCustomerUsers() {
        String url = String.format(Constants.getApiUrl(), "customers/");
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<SingleCustomer> customer = new CasTestUtil().addCustomer(url, SingleCustomer.class, token, customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        String usersEndpoint = url + customerIdentity + "/users/";

        ResponseWrapper<User> user = new CasTestUtil().addUser(usersEndpoint, User.class, token, userName);

        assertThat(user.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));

        String identity = user.getResponseEntity().getResponse().getIdentity();
        String profileIdentity = user.getResponseEntity().getResponse().getUserProfile().getIdentity();
        String patchUrl = usersEndpoint + identity;

        ResponseWrapper<UpdateUser> updatedUser = new CasTestUtil().updateUser(patchUrl, UpdateUser.class, token, userName, identity, customerIdentity, profileIdentity);

        assertThat(updatedUser.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(updatedUser.getResponseEntity().getResponse().getUserProfile().getDepartment(), is(equalTo("QA")));
    }
}
