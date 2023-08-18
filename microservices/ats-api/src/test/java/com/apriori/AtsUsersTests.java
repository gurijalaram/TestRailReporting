package com.apriori;

import com.apriori.ats.models.response.AtsErrorMessage;
import com.apriori.ats.models.response.UserByEmail;
import com.apriori.ats.utils.AtsTestUtil;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AtsUsersTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerIdentity;
    private String userIdentity;

    @AfterEach
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
    @TestRail(id = {3578})
    @Description("Get the current representation of a user identified by their email.")
    public void getUserByEmailTest() {
        String userEmail = "qa-automation-01@apriori.com";
        ResponseWrapper<UserByEmail> user = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, UserByEmail.class, HttpStatus.SC_OK, userEmail);

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
        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, emailPattern);
        userIdentity = user.getResponseEntity().getIdentity();

        atsTestUtil.resetUserMFA(ATSAPIEnum.CUSTOMER_USERS_MFA, customerIdentity, HttpStatus.SC_ACCEPTED);
        atsTestUtil.resetUserMFA(ATSAPIEnum.USER_MFA, userIdentity, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {3579})
    @Description("Update/change the password of a user identified by their email")
    public void changeUserPassword() {
        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();

        customer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, email);
        userIdentity = user.getResponseEntity().getIdentity();
        String userEmail = user.getResponseEntity().getEmail();

        atsTestUtil.changePassword(userEmail);
    }
}