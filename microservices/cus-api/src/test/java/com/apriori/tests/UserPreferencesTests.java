package com.apriori.tests;

import com.apriori.cusapi.entity.request.UpdateUserPrefRequest;
import com.apriori.cusapi.entity.response.PreferenceItemsResponse;
import com.apriori.cusapi.entity.response.PreferenceResponse;
import com.apriori.cusapi.utils.PeopleUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserPreferencesTests {

    private static UserCredentials currentUser;
    SoftAssertions softAssertions = new SoftAssertions();
    private final PeopleUtil peopleUtil = new PeopleUtil();

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = "16826")
    @Description("Verify GET user preferences endpoint test")
    public void verifyUserPrefTest() {

        PreferenceItemsResponse userPreferencesResponse = peopleUtil.getCurrentUserPref(currentUser);

        softAssertions.assertThat(userPreferencesResponse.getItems()).isNotEmpty();
        softAssertions.assertThat(userPreferencesResponse.getPageSize()).isEqualTo(10);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "16828")
    @Description("Verify PATCH(update) user preferences endpoint test")
    public void verifyUpdateUserPrefTest() {
        PreferenceItemsResponse userPreferencesResponse = peopleUtil
            .getCurrentUserPrefParams(currentUser,"pageSize", "100");
        String identity = getItem(userPreferencesResponse,"display.decimalPlaces")
            .getIdentity();
        String value = getItem(userPreferencesResponse,"display.decimalPlaces").getValue();
        String newValue = peopleUtil.ifNumberChangeQty(value);
        Map<String,String> newMap = new HashMap();
        newMap.put(identity, newValue);

        UpdateUserPrefRequest updateUserPrefRequest =
            UpdateUserPrefRequest.builder().userPreferences(newMap).build();

        peopleUtil.updateCurrentUserPref(currentUser, updateUserPrefRequest);

        PreferenceItemsResponse userPreferencesUpdatedResponse = peopleUtil
            .getCurrentUserPrefParams(currentUser,"pageSize", "100");

        softAssertions.assertThat(userPreferencesUpdatedResponse.getItems()).isNotEmpty();
        softAssertions.assertThat(getItem(userPreferencesUpdatedResponse,"display.decimalPlaces").getValue())
            .isEqualTo(newValue);
        softAssertions.assertAll();
    }

    private PreferenceResponse  getItem(PreferenceItemsResponse userPreferencesResponse, String prefName) {
        PreferenceResponse preferenceResponse = userPreferencesResponse.getItems()
            .stream().filter(p -> p.getName().equals(prefName))
            .findFirst()
            .orElse(null);
        return preferenceResponse;
    }
}
