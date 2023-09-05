package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.FailureUpdatePreferencesResponse;
import com.apriori.ach.models.response.Success;
import com.apriori.ach.models.response.SuccessUpdatePreferencesResponse;
import com.apriori.ach.models.response.UserPreferences;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class AchUserPreferencesTests {
    private IdentityHolder userPreferenceIdentityHolder;
    private AchTestUtil achTestUtil = new AchTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
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
    }

    @Test
    @TestRail(id = {21951})
    @Description("Find user preferences for a given user matching a specified query")
    public void getPreferences() {
        ResponseWrapper<UserPreferences> userPreferences = achTestUtil.getCommonRequest(ACHAPIEnum.USER_PREFERENCES, UserPreferences.class, HttpStatus.SC_OK);

        soft.assertThat(userPreferences.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(userPreferences.getResponseEntity().getItems().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21952, 21953})
    @Description("Create and update a UserPreference for a user")
    public void createUpdateUserPreference() {
        String prefName = generateStringUtil.getRandomString();
        String value = generateStringUtil.getRandomNumbersStartsNoZero();
        ResponseWrapper<SuccessUpdatePreferencesResponse> newPreference = achTestUtil.putUserPreference(prefName, value, SuccessUpdatePreferencesResponse.class);

        soft.assertThat(newPreference.getResponseEntity().getSuccesses().get(0).getIdentity()).isNotEmpty();

        String prefIdentity = newPreference.getResponseEntity().getSuccesses().get(0).getIdentity();
        String newValue = generateStringUtil.getRandomNumbersSpecLength(4);

        achTestUtil.updatePreferencesByPatch(prefIdentity, newValue);
        ResponseWrapper<UserPreferences> preferences = achTestUtil.getCommonRequest(ACHAPIEnum.USER_PREFERENCES, UserPreferences.class, HttpStatus.SC_OK);
        String updatedValue = preferences.getResponseEntity().getItems().stream().filter(pref -> pref.getIdentity().equals(prefIdentity)).collect(Collectors.toList()).get(0).getValue();

        soft.assertThat(updatedValue).isEqualTo(newValue);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(achTestUtil.getAprioriInternal().getIdentity())
            .userIdentity(newPreference.getResponseEntity().getSuccesses().get(0).getCreatedBy())
            .userPreferenceIdentity(prefIdentity)
            .build();
    }

    @Test
    @TestRail(id = {22066})
    @Description("Update user preferences by PUT request")
    public void updateUserPreference() {
        String prefName = generateStringUtil.getRandomString();
        String value = generateStringUtil.getRandomNumbersStartsNoZero();
        ResponseWrapper<SuccessUpdatePreferencesResponse> newPreference = achTestUtil.putUserPreference(prefName, value, SuccessUpdatePreferencesResponse.class);
        String prefIdentity = newPreference.getResponseEntity().getSuccesses().get(0).getIdentity();

        String newValue = generateStringUtil.getRandomNumbersStartsNoZero();
        ResponseWrapper<SuccessUpdatePreferencesResponse> updatedPreference = achTestUtil.updatePreferencesByPut(prefName, newValue);
        soft.assertThat(updatedPreference.getResponseEntity().getSuccesses().get(0).getValue().toString()).isEqualTo(newValue);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(achTestUtil.getAprioriInternal().getIdentity())
            .userIdentity(newPreference.getResponseEntity().getSuccesses().get(0).getCreatedBy())
            .userPreferenceIdentity(prefIdentity)
            .build();
    }

    @Test
    @TestRail(id = {22067})
    @Description("Update user preferences with missing field in a body")
    public void failureUpdatePreference() {
        String prefName = generateStringUtil.getRandomString();
        String value = generateStringUtil.getRandomNumbersStartsNoZero();
        Success successResponse = achTestUtil.putUserPreference(prefName, value, SuccessUpdatePreferencesResponse.class)
            .getResponseEntity().getSuccesses().stream().findFirst().get();

        ResponseWrapper<FailureUpdatePreferencesResponse> failureUpdate = achTestUtil.putUserPreference(prefName, value, FailureUpdatePreferencesResponse.class);

        failureUpdate.getResponseEntity().getFailures().forEach(failure -> soft.assertThat(failure.getErrorMessage()).isEqualTo("'updatedBy' should not be null."));
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(achTestUtil.getAprioriInternal().getIdentity())
            .userIdentity(successResponse.getCreatedBy())
            .userPreferenceIdentity(successResponse.getIdentity())
            .build();
    }
}