package com.apriori.cds.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.AccessControls;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsAccessControlsTests  {
    private IdentityHolder accessControlIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerType;
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        customerType = Constants.CLOUD_CUSTOMER;
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (accessControlIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.ACCESS_CONTROL_BY_ID,
                accessControlIdentityHolder.customerIdentity(),
                accessControlIdentityHolder.userIdentity(),
                accessControlIdentityHolder.accessControlIdentity()
            );
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {3294})
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

        soft.assertThat(accessControlResponse.getResponseEntity().getOutOfContext()).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3290})
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

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(accessControls.getResponseEntity().getTotalItemCount()).isEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3292})
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

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROL_BY_ID, AccessControlResponse.class, HttpStatus.SC_OK, customerIdentity, userIdentity, accessControlIdentity);

        soft.assertThat(accessControlResponse.getResponseEntity().getUserIdentity()).isEqualTo(userIdentity);
        soft.assertAll();
    }
}
