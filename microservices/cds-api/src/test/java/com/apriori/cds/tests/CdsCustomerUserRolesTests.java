package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserRole;
import com.apriori.cds.objects.response.UserRoles;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Before
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

    @After
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"13414"})
    @Description("Returns a list of roles for a user")
    public void getUserRoles() {
        ResponseWrapper<UserRoles> userRoles = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES, UserRoles.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        assertThat(userRoles.getResponseEntity().getTotalItemCount(), greaterThanOrEqualTo(1));
    }

    @Test
    @TestRail(testCaseId = {"13415", "13417", "13422"})
    @Description("Create a role for a user, gets it by identity and delete")
    public void postUserRoles() {
        ResponseWrapper<UserRole> newRole = cdsTestUtil.createRoleForUser(customerIdentity, userIdentity);

        assertThat(newRole.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String roleId = newRole.getResponseEntity().getIdentity();

        ResponseWrapper<UserRole> userRole = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES_BY_ID, UserRole.class, HttpStatus.SC_OK, customerIdentity, userIdentity, roleId);

        assertThat(userRole.getResponseEntity().getIdentity(), is(equalTo(roleId)));

        ResponseWrapper<String> deleteRole = cdsTestUtil.delete(CDSAPIEnum.USER_ROLES_BY_ID, customerIdentity, userIdentity, roleId);

        assertThat(deleteRole.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
