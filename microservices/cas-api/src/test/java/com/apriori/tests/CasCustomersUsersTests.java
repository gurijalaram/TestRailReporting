package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.IdentityHolder;
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
    private IdentityHolder userIdentityHolder;
    private IdentityHolder customerIdentityHolder;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @After
    public void cleanUp() {
        if (userIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                    userIdentityHolder.customerIdentity(),
                    userIdentityHolder.userIdentity()
            );
            if (customerIdentityHolder != null) {
                cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID,
                        customerIdentityHolder.customerIdentity()
                );
            }
        }
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

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .build();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        soft.assertThat(user.getStatusCode())
            .isEqualTo(HttpStatus.SC_CREATED);
        soft.assertThat(user.getResponseEntity().getUsername())
            .isEqualTo(userName);

        ResponseWrapper<CustomerUsers> customerUsers = casTestUtil.getCommonRequest(CASAPIEnum.USERS, CustomerUsers.class, customerIdentity);

        soft.assertThat(customerUsers.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(customerUsers.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String userIdentity = customerUsers.getResponseEntity().getItems().get(0).getIdentity();
        userIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .build();

        ResponseWrapper<CustomerUser> singleUser = casTestUtil.getCommonRequest(CASAPIEnum.USER, CustomerUser.class, customerIdentity, userIdentity);

        soft.assertThat(singleUser.getResponseEntity().getIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();
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

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .build();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        soft.assertThat(user.getResponseEntity().getUsername())
            .isEqualTo(userName);

        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .build();
        String profileIdentity = user.getResponseEntity().getUserProfile().getIdentity();

        ResponseWrapper<UpdateUser> updatedUser = CasTestUtil.updateUser(userName, customerName, userIdentity, customerIdentity, profileIdentity);

        soft.assertThat(updatedUser.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment())
            .isEqualTo("QA");
        soft.assertAll();
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

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .build();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);

        soft.assertThat(user.getResponseEntity().getUsername())
            .isEqualTo(userName);

        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .build();

        ResponseWrapper<String> resetMfa = CasTestUtil.resetUserMfa(customerIdentity, userIdentity);

        soft.assertThat(resetMfa.getStatusCode())
            .isEqualTo(HttpStatus.SC_ACCEPTED);
        soft.assertAll();
    }
}
