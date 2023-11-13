package com.apriori.qms.api.tests;

import com.apriori.qms.api.controller.QmsUserPreferenceResources;
import com.apriori.qms.api.models.request.userpreference.UserPreferenceParameters;
import com.apriori.qms.api.models.request.userpreference.UserPreferenceRequest;
import com.apriori.qms.api.models.response.userpreference.UserPreferenceResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
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
public class QmsUserPreferenceTest extends TestUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {16854})
    @Description("Verify that user can get user preference")
    public void verifyGetUserPreference() {
        UserPreferenceResponse userPreferenceResponse =
            QmsUserPreferenceResources.getUserPreference(UserPreferenceResponse.class, currentUser, HttpStatus.SC_OK);
        softAssertions.assertThat(userPreferenceResponse.getIdentity()).isNotEmpty();
    }

    @Test
    @TestRail(id = {16855})
    @Description("Verify that user can update user preference")
    public void verifyUpdateUserPreference() {
        UserPreferenceRequest userPreferenceRequestBuilder = UserPreferenceRequest.builder()
            .userPreferences(UserPreferenceParameters.builder().avatarColor("#FF015").build())
            .build();
        UserPreferenceResponse userPreferenceResponse =
            QmsUserPreferenceResources.updateUserPreference(userPreferenceRequestBuilder, UserPreferenceResponse.class, currentUser, HttpStatus.SC_OK);
        softAssertions.assertThat(userPreferenceResponse.getAvatarColor())
            .isEqualTo(userPreferenceRequestBuilder.getUserPreferences().getAvatarColor());
    }

    @Test
    @TestRail(id = {16856})
    @Description("Verify that user can delete user preference")
    public void verifyDeleteUserPreference() {
        QmsUserPreferenceResources.deleteUserPreference(currentUser, HttpStatus.SC_NO_CONTENT);
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}