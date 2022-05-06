package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.UserPreference;
import com.apriori.cds.objects.response.UserPreferences;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsUserPreferencesTests {
    private IdentityHolder userPreferenceIdentityHolder;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static ResponseWrapper<User> user;
    private static String customerName;
    private static String userName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String userIdentity;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @After
    public void deletePreferences() {
        if (userPreferenceIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID,
                userPreferenceIdentityHolder.customerIdentity(),
                userPreferenceIdentityHolder.userIdentity(),
                userPreferenceIdentityHolder.userPreferenceIdentity()
            );
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12397"})
    @Description("Returns a paged set of UserPreferences for a specific user.")
    public void getUserPreferences() {
        ResponseWrapper<UserPreferences> userPreferences = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_PREFERENCES, UserPreferences.class, customerIdentity, userIdentity);

        assertThat(userPreferences.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(userPreferences.getResponseEntity().getTotalItemCount(), greaterThanOrEqualTo(1));
    }

    @Test
    @TestRail(testCaseId = {"12398", "12399"})
    @Description("Creates a user preference for a user and gets it by identity")
    public void addUserPreference() {
        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);

        assertThat(newPreference.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class, customerIdentity, userIdentity, preferenceIdentity);

        assertThat(preferenceResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(preferenceResponse.getResponseEntity().getIdentity(), is(equalTo(preferenceIdentity)));

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"12400"})
    @Description("Updates an existing user preference by identity")
    public void updateUserPreference() {
        String updatedPreference = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> updatedPreferenceResponse = cdsTestUtil.updatePreference(customerIdentity, userIdentity, preferenceIdentity, updatedPreference);

        assertThat(updatedPreferenceResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(updatedPreferenceResponse.getResponseEntity().getValue(), is(equalTo(updatedPreference)));

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"12401", "12402"})
    @Description("Adds or Replaces a UserPreference for a user")
    public void putUserPreference() {
        String preferenceName = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.putUserPreference(customerIdentity, userIdentity, preferenceName);

        assertThat(preferenceResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String preferenceIdentity = preferenceResponse.getResponseEntity().getIdentity();

        ResponseWrapper<String> deletePreference = cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID, customerIdentity, userIdentity, preferenceIdentity);

        assertThat(deletePreference.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}