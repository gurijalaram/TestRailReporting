package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsCustomerUsersTests {
    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (userIdentityEndpoint != null) {
            cdsTestUtil.delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"3293"})
    @Description("Add a user to a customer")
    public void addCustomerUsers() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));
    }

    @Test
    @TestRail(testCaseId = {"3250"})
    @Description("Get a list of users for a customer")
    public void getCustomerUsers() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String usersEndpoint = String.format(url, String.format("customers/%s/users", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Users> response = cdsTestUtil.getCommonRequest(usersEndpoint, Users.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"3281"})
    @Description("Add a user to a customer")
    public void getCustomerUserByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<User> response = cdsTestUtil.getCommonRequest(userIdentityEndpoint, User.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getIdentity(), is(equalTo(userIdentity)));
        assertThat(response.getResponseEntity().getResponse().getUsername(), is(equalTo(userName)));
    }

    @Test
    @TestRail(testCaseId = {"3295"})
    @Description("Update a user")
    public void patchUserByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<User> patchResponse = cdsTestUtil.patchUser(customerIdentity, userIdentity);
        assertThat(patchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(patchResponse.getResponseEntity().getResponse().getUserProfile().getDepartment(), is(equalTo("Design Dept")));
    }

    @Test
    @TestRail(testCaseId = {"5967"})
    @Description("Delete user wrong identity")
    public void deleteWrongUserIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));
        String wrongIdentityEndpoint = String.format(url, String.format("customers/%s/users/L2H992829CFB", customerIdentity));

        RequestEntity requestEntity = RequestEntity.init(wrongIdentityEndpoint, ErrorMessage.class);
        ResponseWrapper<ErrorMessage> responseWrapper = GenericRequestUtil.delete(requestEntity, new RequestAreaApi());
        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        assertThat(responseWrapper.getResponseEntity().getMessage(), is(containsString("Unable to get user with identity")));
    }
}