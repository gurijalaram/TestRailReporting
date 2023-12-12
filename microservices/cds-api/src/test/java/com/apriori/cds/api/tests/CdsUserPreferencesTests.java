package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.UserPreference;
import com.apriori.cds.api.models.response.UserPreferences;
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
public class CdsUserPreferencesTests {
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder userPreferenceIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;

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
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {12397})
    @Description("Returns a paged set of UserPreferences for a specific user.")
    public void getUserPreferences() {
        setCustomerData();
        ResponseWrapper<UserPreferences> userPreferences = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_PREFERENCES, UserPreferences.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userPreferences.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {12398, 12399})
    @Description("Creates a user preference for a user and gets it by identity")
    public void addUserPreference() {
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
        String preferenceName = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.putUserPreference(customerIdentity, userIdentity, preferenceName);

        soft.assertThat(preferenceResponse.getResponseEntity().getName()).isEqualTo(preferenceName);
        soft.assertAll();

        String preferenceIdentity = preferenceResponse.getResponseEntity().getIdentity();

        cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID, customerIdentity, userIdentity, preferenceIdentity);
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