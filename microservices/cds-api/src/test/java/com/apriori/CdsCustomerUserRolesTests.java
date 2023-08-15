package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.Customer;
import com.apriori.cds.models.response.ErrorResponse;
import com.apriori.cds.models.response.User;
import com.apriori.cds.models.response.UserRole;
import com.apriori.cds.models.response.UserRoles;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdsCustomerUserRolesTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerName;
    private String userName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String userIdentity;
    private final String role = "AP_USER_ADMIN";
    private final String invalidRole = "ADMIN";
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {13414})
    @Description("Returns a list of roles for a user")
    public void getUserRoles() {
        ResponseWrapper<UserRoles> userRoles = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES, UserRoles.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userRoles.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13415, 13417, 13422, 17166})
    @Description("Create a role for a user, gets it by identity and delete")
    public void postUserRoles() {
        ResponseWrapper<UserRole> newRole = cdsTestUtil.createRoleForUser(customerIdentity, userIdentity, role);
        String roleId = newRole.getResponseEntity().getIdentity();

        ResponseWrapper<UserRole> userRole = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES_BY_ID, UserRole.class, HttpStatus.SC_OK, customerIdentity, userIdentity, roleId);

        soft.assertThat(userRole.getResponseEntity().getIdentity()).isEqualTo(roleId);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.USER_ROLES_BY_ID, customerIdentity, userIdentity, roleId);
    }

    @Test
    @TestRail(id = {17165})
    @Description("Try to create an invalid role (without AP_* prefix) for a user and verify that it fails")
    public void postInvalidUserRoles() {
        String expectedMessage = "Resource 'Role' with identity 'ADMIN' was not found";
        String expectedError = "Not Found";
        ErrorResponse response = cdsTestUtil.createInvalidRoleForUser(customerIdentity, userIdentity,invalidRole);

        soft.assertThat(response.getMessage()).isEqualTo(expectedMessage);
        soft.assertThat(response.getError()).isEqualTo(expectedError);
        soft.assertAll();
    }
}
