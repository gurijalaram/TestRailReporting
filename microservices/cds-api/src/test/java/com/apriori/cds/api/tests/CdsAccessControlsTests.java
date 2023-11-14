package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsAccessControlsTests {
    private IdentityHolder accessControlIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();

    @AfterEach
    public void cleanUp() {
        if (accessControlIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.ACCESS_CONTROL_BY_ID,
                accessControlIdentityHolder.customerIdentity(),
                accessControlIdentityHolder.userIdentity(),
                accessControlIdentityHolder.accessControlIdentity()
            );
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
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
        setCustomerData();
        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
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
        setCustomerData();
        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(accessControls.getResponseEntity().getTotalItemCount()).isEqualTo(4);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3292})
    @Description("Get access control by Control ID")
    public void getAccessControlById() {
        setCustomerData();
        ResponseWrapper<AccessControlResponse> accessControl = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControl.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROL_BY_ID, AccessControlResponse.class, HttpStatus.SC_OK, customerIdentity, userIdentity, accessControlIdentity);

        soft.assertThat(accessControlResponse.getResponseEntity().getUserIdentity()).isEqualTo(userIdentity);
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
