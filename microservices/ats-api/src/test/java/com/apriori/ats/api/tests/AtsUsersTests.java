package com.apriori.ats.api.tests;

import com.apriori.ats.api.models.response.AtsErrorMessage;
import com.apriori.ats.api.utils.AtsTestUtil;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
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
public class AtsUsersTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private ResponseWrapper<User> user;
    private String customerIdentity;
    private String userIdentity;

    @AfterEach
    public void cleanUp() {
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
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
    @TestRail(id = {3578})
    @Description("Get the current representation of a user identified by their email.")
    public void getUserByEmailTest() {
        String userEmail = "qa-automation-012222@apriori.com";
        ResponseWrapper<User> user = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, User.class, HttpStatus.SC_OK, userEmail);

        soft.assertThat(user.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3668})
    @Description("Get user by invalid email")
    public void getUserInvalidEmail() {
        String invalidEmail = "qa-automation-01-apriori.com";
        ResponseWrapper<AtsErrorMessage> errorResponse = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, AtsErrorMessage.class, HttpStatus.SC_BAD_REQUEST, invalidEmail);

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("'email' is not a valid email address.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {22086, 22087})
    @Description("Reset the MFA configuration for a user.")
    public void resetUserMFA() {
        setCustomerData();
        atsTestUtil.resetUserMFA(ATSAPIEnum.CUSTOMER_USERS_MFA, customerIdentity, HttpStatus.SC_ACCEPTED);
        atsTestUtil.resetUserMFA(ATSAPIEnum.USER_MFA, userIdentity, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {3579})
    @Description("Update/change the password of a user identified by their email")
    public void changeUserPassword() {
        setCustomerData();
        String userEmail = user.getResponseEntity().getEmail();

        atsTestUtil.changePassword(userEmail);
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
        String userName = generateStringUtil.generateUserName();

        user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}