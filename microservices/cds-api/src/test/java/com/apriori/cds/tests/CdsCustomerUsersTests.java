package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsCustomerUsersTests {
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String userIdentity;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3293"})
    @Description("Add a user to a customer")
    public void addCustomerUsers() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(user.getResponseEntity().getUsername(), is(equalTo(userName)));
    }

    @Test
    @TestRail(testCaseId = {"3250"})
    @Description("Get a list of users for a customer")
    public void getCustomerUsers() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<Users> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USERS_BY_CUSTOMER_ID, Users.class, customerIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getIdentity(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"3281"})
    @Description("Add a user to a customer")
    public void getCustomerUserByIdentity() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<User> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USERS_BY_CUSTOMER_USER_IDS, User.class, customerIdentity, userIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getIdentity(), is(equalTo(userIdentity)));
        assertThat(response.getResponseEntity().getUsername(), is(equalTo(userName)));
    }

    @Test
    @TestRail(testCaseId = {"3295"})
    @Description("Update a user")
    public void patchUserByIdentity() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<User> patchResponse = cdsTestUtil.patchUser(customerIdentity, userIdentity);
        assertThat(patchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(patchResponse.getResponseEntity().getUserProfile().getDepartment(), is(equalTo("Design Dept")));
    }

    @Test
    @TestRail(testCaseId = {"5967"})
    @Description("Delete user wrong identity")
    public void deleteWrongUserIdentity() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        userIdentity = user.getResponseEntity().getIdentity();

        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.DELETE_USER_BY_CUSTOMER_ID, ErrorMessage.class)
            .inlineVariables(customerIdentity);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();
        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        assertThat(responseWrapper.getResponseEntity().getMessage(), is(containsString("Unable to get user with identity")));
    }
}
