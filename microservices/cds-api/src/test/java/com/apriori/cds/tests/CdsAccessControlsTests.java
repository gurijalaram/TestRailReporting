package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.AccessControls;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsAccessControlsTests  {
    private static IdentityHolder accessControlIdentityHolder;
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
        if (accessControlIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_ACCESS_CONTROL_BY_CUSTOMER_USER_CONTROL_IDS,
                accessControlIdentityHolder.customerIdentity(),
                accessControlIdentityHolder.userIdentity(),
                accessControlIdentityHolder.accessControlIdentity()
            );
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3294"})
    @Description("Adding out of context access control")
    public void postAccessControl() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getIdentity();

        accessControlIdentityHolder =  IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        assertThat(accessControlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(accessControlResponse.getResponseEntity().getOutOfContext(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3290"})
    @Description("Get Access controls by Customer and User")
    public void getAccessControl() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getIdentity();

        accessControlIdentityHolder =  IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .accessControlIdentity(accessControlIdentity)
                .build();

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_ACCESS_CONTROLS, AccessControls.class, customerIdentity, userIdentity);

        assertThat(accessControls.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(accessControls.getResponseEntity().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"3292"})
    @Description("Get access control by Control ID")
    public void getAccessControlById() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControlResponse> accessControl = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControl.getResponseEntity().getIdentity();

        accessControlIdentityHolder =  IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .accessControlIdentity(accessControlIdentity)
                .build();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_ACCESS_CONTROL_BY_ID, AccessControlResponse.class, customerIdentity, userIdentity, accessControlIdentity);

        assertThat(accessControlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(accessControlResponse.getResponseEntity().getUserIdentity(), is(equalTo(userIdentity)));
    }
}
