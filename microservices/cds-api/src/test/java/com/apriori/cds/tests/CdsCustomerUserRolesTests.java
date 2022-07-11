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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsCustomerUserRolesTests {
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static ResponseWrapper<User> user;
    private static String customerName;
    private static String userName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String userIdentity;

    @BeforeClass
    public static void setDetails() {
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

    @AfterClass
    public static void cleanUp() {
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
        ResponseWrapper<UserRoles> userRoles = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES, UserRoles.class, customerIdentity, userIdentity);

        assertThat(userRoles.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(userRoles.getResponseEntity().getTotalItemCount(), greaterThanOrEqualTo(1));
    }

    @Test
    @TestRail(testCaseId = {"13415", "13417", "13422"})
    @Description("Create a role for a user, gets it by identity and delete")
    public void postUserRoles() {
        ResponseWrapper<UserRole> newRole = cdsTestUtil.createRoleForUser(customerIdentity, userIdentity);

        assertThat(newRole.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String roleId = newRole.getResponseEntity().getIdentity();

        ResponseWrapper<UserRole> userRole = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES_BY_ID, UserRole.class, customerIdentity, userIdentity, roleId);

        assertThat(userRole.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(userRole.getResponseEntity().getIdentity(), is(equalTo(roleId)));

        ResponseWrapper<String> deleteRole = cdsTestUtil.delete(CDSAPIEnum.USER_ROLES_BY_ID, customerIdentity, userIdentity, roleId);

        assertThat(deleteRole.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
