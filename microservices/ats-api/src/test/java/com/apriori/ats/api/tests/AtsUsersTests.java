package com.apriori.ats.api.tests;

import com.apriori.ats.api.models.response.AtsErrorMessage;
import com.apriori.ats.api.utils.AtsUtil;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CdsUserUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
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
public class AtsUsersTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private AtsUtil atsUtil;
    private SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil;
    private CdsUserUtil cdsUserUtil;
    private CustomerInfrastructure customerInfrastructure;
    private CustomerUtil customerUtil;
    private ResponseWrapper<User> user;
    private String customerIdentity;
    private String userIdentity;
    private UserCredentials userCreds;

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);
        cdsUserUtil = new CdsUserUtil(requestEntityUtil);
        atsUtil = new AtsUtil(requestEntityUtil);
        userCreds = requestEntityUtil.getEmbeddedUser();
        customerUtil = new CustomerUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @TestRail(id = {3578})
    @Description("Get the current representation of a user identified by their email.")
    public void getUserByEmailTest() {
        String userEmail = userCreds.getEmail();
        ResponseWrapper<User> user = atsUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, User.class, HttpStatus.SC_OK, userEmail);

        soft.assertThat(user.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3668})
    @Description("Get user by invalid email")
    public void getUserInvalidEmail() {
        String invalidEmail = "qa-automation-01-apriori.com";
        ResponseWrapper<AtsErrorMessage> errorResponse = atsUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, AtsErrorMessage.class, HttpStatus.SC_BAD_REQUEST, invalidEmail);

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("'email' is not a valid email address.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {22086, 22087})
    @Description("Reset the MFA configuration for a user.")
    public void resetUserMFA() {
        setCustomerData();
        atsUtil.resetUserMFA(ATSAPIEnum.CUSTOMER_USERS_MFA, customerIdentity, HttpStatus.SC_ACCEPTED);
        atsUtil.resetUserMFA(ATSAPIEnum.USER_MFA, userIdentity, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {3579})
    @Description("Update/change the password of a user identified by their email")
    public void changeUserPassword() {
        setCustomerData();
        String userEmail = user.getResponseEntity().getEmail();

        atsUtil.changePassword(userEmail);
    }

    private void setCustomerData() {
        RandomCustomerData randomCustomerData = new RandomCustomerData();
        ResponseWrapper<Customer> customer = customerUtil.addCustomer(randomCustomerData);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(randomCustomerData, customerIdentity);
        String userName = generateStringUtil.generateUserName();

        user = cdsUserUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}