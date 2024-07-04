package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.ErrorResponse;
import com.apriori.cds.api.models.response.UserRole;
import com.apriori.cds.api.models.response.UserRoles;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CdsUserUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomerUserRolesTests {
    private final String role = "AP_USER_ADMIN";
    private final String invalidRole = "ADMIN";
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure;
    private CdsTestUtil cdsTestUtil;
    private CustomerUtil customerUtil;
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();
    private CdsUserUtil cdsUserUtil;

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);
        cdsUserUtil = new CdsUserUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {13414})
    @Description("Returns a list of roles for a user")
    public void getUserRoles() {
        setCustomerData();
        ResponseWrapper<UserRoles> userRoles = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES, UserRoles.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userRoles.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13415, 13417, 13422, 17166})
    @Description("Create a role for a user, gets it by identity and delete")
    public void postUserRoles() {
        setCustomerData();
        UserRole newRole = cdsUserUtil.createRoleForUser(role, customerIdentity, userIdentity);
        String roleId = newRole.getIdentity();

        ResponseWrapper<UserRole> userRole = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES_BY_ID, UserRole.class, HttpStatus.SC_OK, customerIdentity, userIdentity, roleId);

        soft.assertThat(userRole.getResponseEntity().getIdentity()).isEqualTo(roleId);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.USER_ROLES_BY_ID, customerIdentity, userIdentity, roleId);
    }

    @Test
    @TestRail(id = {17165})
    @Description("Try to create an invalid role (without AP_* prefix) for a user and verify that it fails")
    public void postInvalidUserRoles() {
        setCustomerData();
        String expectedMessage = "Resource 'Role' with identity 'ADMIN' was not found";
        String expectedError = "Not Found";
        ErrorResponse response = cdsUserUtil.createInvalidRoleForUser(invalidRole, customerIdentity, userIdentity);

        soft.assertThat(response.getMessage()).isEqualTo(expectedMessage);
        soft.assertThat(response.getError()).isEqualTo(expectedError);
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = customerUtil.addCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsUserUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
