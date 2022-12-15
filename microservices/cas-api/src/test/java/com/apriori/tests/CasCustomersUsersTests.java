package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.entity.response.UpdateUser;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CasCustomersUsersTests {
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private Customer newCustomer;
    private String customerIdentity;
    private String userIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();
    }

    @After
    public void cleanUp() {
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @TestRail(testCaseId = {"5661", "5662", "5663"})
    @Description("Add a user to a customer, return a list of users for the customer, get the User identified by its identity.")
    public void addCustomerUsers() {

        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        soft.assertThat(user.getResponseEntity().getCustomerIdentity())
            .isEqualTo(customerIdentity);

        ResponseWrapper<CustomerUsers> customerUsers = casTestUtil.getCommonRequest(CASAPIEnum.USERS, CustomerUsers.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(customerUsers.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        userIdentity = customerUsers.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<CustomerUser> singleUser = casTestUtil.getCommonRequest(CASAPIEnum.USER, CustomerUser.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(singleUser.getResponseEntity().getIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5664"})
    @Description("Update the User.")
    public void updateCustomerUsers() {
        String customerName = newCustomer.getName();

        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();
        String profileIdentity = user.getResponseEntity().getUserProfile().getIdentity();
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<UpdateUser> updatedUser = CasTestUtil.updateUser(userName, customerName, userIdentity, customerIdentity, profileIdentity);

        soft.assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment())
            .isEqualTo("QA");
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5667"})
    @Description("Reset the MFA configuration for a user.")
    public void resettingUserMfa() {
        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();

        CasTestUtil.resetUserMfa(customerIdentity, userIdentity);
    }

    @Test
    @TestRail(testCaseId = {"16378", "16379"})
    @Description("Export users template and export customer users")
    public void exportUsers() {
        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<String> template = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_TEMPLATE, null, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(template).isNotNull();

        ResponseWrapper<String> usersExport = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_USERS, null, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(usersExport).isNotNull();
        soft.assertThat(usersExport.getBody()).contains(userName);
        soft.assertAll();
    }
}
