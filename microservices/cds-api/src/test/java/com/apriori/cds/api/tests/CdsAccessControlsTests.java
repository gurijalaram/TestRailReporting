package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.cds.api.utils.AccessControlUtil;
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
public class CdsAccessControlsTests {
    private IdentityHolder accessControlIdentityHolder;
    private final GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure;
    private CustomerUtil customerUtil;
    private CdsTestUtil cdsTestUtil;
    private AccessControlUtil accessControlUtil;
    private CdsUserUtil cdsUserUtil;
    private String customerIdentity;
    private String userIdentity;
    private final SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        accessControlUtil = new AccessControlUtil(requestEntityUtil);
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);
        cdsUserUtil = new CdsUserUtil(requestEntityUtil);
    }

    @AfterEach
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
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {3294})
    @Description("Adding out of context access control")
    public void postAccessControl() {
        setCustomerData();
        ResponseWrapper<AccessControlResponse> accessControlResponse = accessControlUtil.addAccessControl(customerIdentity, userIdentity);
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
        ResponseWrapper<AccessControlResponse> accessControlResponse = accessControlUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(accessControls.getResponseEntity().getTotalItemCount()).isEqualTo(3);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3292})
    @Description("Get access control by Control ID")
    public void getAccessControlById() {
        setCustomerData();
        ResponseWrapper<AccessControlResponse> accessControl = accessControlUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControl.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROL_BY_ID, AccessControlResponse.class,
            HttpStatus.SC_OK, customerIdentity, userIdentity, accessControlIdentity);

        soft.assertThat(accessControlResponse.getResponseEntity().getUserIdentity()).isEqualTo(userIdentity);
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
