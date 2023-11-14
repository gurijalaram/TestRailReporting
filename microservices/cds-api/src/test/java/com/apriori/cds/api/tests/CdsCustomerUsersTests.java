package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.CredentialsItems;
import com.apriori.cds.api.models.response.UserProperties;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomerUsersTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private String customerName;
    private String userIdentity;

    @AfterEach
    public void cleanUp() {
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {3293})
    @Description("Add a user to a customer")
    public void addCustomerUsers() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        soft.assertThat(user.getResponseEntity().getUsername()).isEqualTo(userName);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3250})
    @Description("Get a list of users for a customer")
    public void getCustomerUsers() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<Users> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3281})
    @Description("Add a user to a customer")
    public void getCustomerUserByIdentity() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<User> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(response.getResponseEntity().getIdentity()).isEqualTo(userIdentity);
        soft.assertThat(response.getResponseEntity().getUsername()).isEqualTo(userName);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3295})
    @Description("Update a user")
    public void patchUserByIdentity() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        User userResponse = user.getResponseEntity();

        ResponseWrapper<User> patchResponse = cdsTestUtil.patchUser(userResponse);

        soft.assertThat(patchResponse.getResponseEntity().getUserProfile().getDepartment()).isEqualTo("Design Dept");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5967})
    @Description("Delete user wrong identity")
    public void deleteWrongUserIdentity() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        RequestEntity requestEntity = RequestEntityUtil.init(CDSAPIEnum.DELETE_USER_WRONG_ID, ErrorMessage.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();
        soft.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("Unable to get user with identity");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13304})
    @Description("Updates/changes the credentials for the user identified by their identity")
    public void updateUserCredentials() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<CredentialsItems> credentials = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_CREDENTIALS_BY_ID, CredentialsItems.class, HttpStatus.SC_OK, userIdentity);
        String currentHashPassword = credentials.getResponseEntity().getPasswordHash();
        String currentPasswordSalt = credentials.getResponseEntity().getPasswordSalt();

        ResponseWrapper<CredentialsItems> updatedCredentials = cdsTestUtil.updateUserCredentials(customerIdentity, userIdentity, currentHashPassword, currentPasswordSalt);

        soft.assertThat(updatedCredentials.getResponseEntity().getPasswordHashHistory().get(0)).isEqualTo(currentHashPassword);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {24489})
    @Description("GET Required User Properties")
    public void getUserProperties() {
        setCustomerData();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, generateStringUtil.generateUserName(), customerName);
        userIdentity = user.getResponseEntity().getIdentity();
        cdsTestUtil.createRoleForUser(customerIdentity, userIdentity, "AP_DESIGNER");

        ResponseWrapper<UserProperties> requiredUserProperties = cdsTestUtil.getCommonRequest(CDSAPIEnum.REQUIRED_USER_PROPERTIES, UserProperties.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(requiredUserProperties.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(requiredUserProperties.getResponseEntity().getItems().get(0).getName()).isNotEmpty();
        soft.assertThat(requiredUserProperties.getResponseEntity().getItems().get(0).getSource()).isNotEmpty();
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerName = customer.getResponseEntity().getName();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
    }
}
