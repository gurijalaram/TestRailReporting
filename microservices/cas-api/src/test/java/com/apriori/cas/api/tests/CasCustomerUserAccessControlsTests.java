package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.AccessControl;
import com.apriori.cas.api.models.response.AccessControls;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
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

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CasCustomerUserAccessControlsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final SoftAssertions soft = new SoftAssertions();
    private final GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private IdentityHolder accessControlIdentityHolder;
    private ResponseWrapper<Customer> customer;
    private String customerIdentity;
    private String userIdentity;
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

    }

    @AfterEach
    public void cleanUp() {
        if (accessControlIdentityHolder != null) {
            casTestUtil.delete(CASAPIEnum.ACCESS_CONTROL_BY_ID,
                accessControlIdentityHolder.customerIdentity(),
                accessControlIdentityHolder.userIdentity(),
                accessControlIdentityHolder.accessControlIdentity()
            );
        }
        if (customerIdentity != null && userIdentity != null) {
            casTestUtil.delete(CASAPIEnum.USER, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Description("POSTs a new access control for a user.")
    @TestRail(id = {16144})
    public void postAccessControl() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControl> accessControl = casTestUtil.addAccessControl(customerIdentity, userIdentity);

        soft.assertThat(accessControl.getResponseEntity().getOutOfContext())
            .isTrue();
        soft.assertAll();

        String accessControlIdentity = accessControl.getResponseEntity().getIdentity();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();
    }

    @Test
    @Description("Returns a list of access controls for the customer user")
    @TestRail(id = {16145})
    public void getUserAccessControls() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControl> accessControl = casTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlId = accessControl.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControls> listOfControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
            customerIdentity,
            userIdentity);

        soft.assertThat(listOfControls.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertThat(listOfControls.getResponseEntity().getItems().stream().filter(ac -> ac.getApplicationName().equals("aP Workspace")).collect(Collectors.toList()).get(0).getIdentity())
            .isEqualTo(accessControlId);
        soft.assertAll();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlId)
            .build();
    }

    @Test
    @Description("Get a specific access control for a customer user identified by its identity")
    @TestRail(id = {16146})
    public void getAccessControlByIdentity() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControl> accessControl = casTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlId = accessControl.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControl> controlById = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROL_BY_ID, AccessControl.class, HttpStatus.SC_OK,
            customerIdentity,
            userIdentity,
            accessControlId);

        soft.assertThat(controlById.getResponseEntity().getUserIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();

        accessControlIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlId)
            .build();
    }

    @Test
    @Description("Delete an access control for a user")
    @TestRail(id = 16147)
    public void deleteAccessControl() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControl> accessControl = casTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlId = accessControl.getResponseEntity().getIdentity();

        casTestUtil.delete(CASAPIEnum.ACCESS_CONTROL_BY_ID,
            customerIdentity,
            userIdentity,
            accessControlId);
    }

    @Test
    @Description("Get the customer user access control with not existing identity")
    @TestRail(id = {16150})
    public void getAccessControlThatNotExists() {
        String notExistingIdentity = "000000000000";
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<CasErrorMessage> response = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROL_BY_ID,
            CasErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            customerIdentity,
            userIdentity,
            notExistingIdentity);

        soft.assertThat(response.getResponseEntity().getMessage())
            .isEqualTo(String.format("Unable to get access control with identity '%s' for user with identity '%s'.", notExistingIdentity, userIdentity));
        soft.assertAll();
    }
}