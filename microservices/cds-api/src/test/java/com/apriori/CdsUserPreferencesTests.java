package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserPreference;
import com.apriori.cds.objects.response.UserPreferences;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdsUserPreferencesTests {
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder userPreferenceIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerName;
    private String userName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String userIdentity;

    @BeforeEach
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void deletePreferences() {
        if (userPreferenceIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID,
                userPreferenceIdentityHolder.customerIdentity(),
                userPreferenceIdentityHolder.userIdentity(),
                userPreferenceIdentityHolder.userPreferenceIdentity()
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
    @TestRail(id = {12397})
    @Description("Returns a paged set of UserPreferences for a specific user.")
    public void getUserPreferences() {
        ResponseWrapper<UserPreferences> userPreferences = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_PREFERENCES, UserPreferences.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userPreferences.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {12398, 12399})
    @Description("Creates a user preference for a user and gets it by identity")
    public void addUserPreference() {
        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class, HttpStatus.SC_OK, customerIdentity, userIdentity, preferenceIdentity);

        soft.assertThat(preferenceResponse.getResponseEntity().getIdentity()).isEqualTo(preferenceIdentity);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(id = {12400})
    @Description("Updates an existing user preference by identity")
    public void updateUserPreference() {
        String updatedPreference = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> updatedPreferenceResponse = cdsTestUtil.updatePreference(customerIdentity, userIdentity, preferenceIdentity, updatedPreference);

        soft.assertThat(updatedPreferenceResponse.getResponseEntity().getValue()).isEqualTo(updatedPreference);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(id = {12401, 12402})
    @Description("Adds or Replaces a UserPreference for a user")
    public void putUserPreference() {
        String preferenceName = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.putUserPreference(customerIdentity, userIdentity, preferenceName);

        soft.assertThat(preferenceResponse.getResponseEntity().getName()).isEqualTo(preferenceName);
        soft.assertAll();

        String preferenceIdentity = preferenceResponse.getResponseEntity().getIdentity();

        cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID, customerIdentity, userIdentity, preferenceIdentity);
    }
}