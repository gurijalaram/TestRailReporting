package com.apriori;

import com.apriori.cus.models.request.UpdateUserPrefRequest;
import com.apriori.cus.models.response.PreferenceItemsResponse;
import com.apriori.cus.models.response.PreferenceResponse;
import com.apriori.cus.utils.PeopleUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestRulesApi.class)
public class UserPreferencesTests {

    private static UserCredentials currentUser;
    SoftAssertions softAssertions = new SoftAssertions();
    private final PeopleUtil peopleUtil = new PeopleUtil();

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = 16826)
    @Description("Verify GET user preferences endpoint test")
    public void verifyUserPrefTest() {

        PreferenceItemsResponse userPreferencesResponse = peopleUtil.getCurrentUserPref(currentUser);

        softAssertions.assertThat(userPreferencesResponse.getItems()).isNotEmpty();
        softAssertions.assertThat(userPreferencesResponse.getPageSize()).isEqualTo(10);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 16828)
    @Description("Verify PATCH(update) user preferences endpoint test")
    public void verifyUpdateUserPrefTest() {
        PreferenceItemsResponse userPreferencesResponse = peopleUtil
            .getCurrentUserPrefParams(currentUser,"pageSize", "100");
        String identity = getItem(userPreferencesResponse,"display.decimalPlaces")
            .getIdentity();
        String value = getItem(userPreferencesResponse,"display.decimalPlaces").getValue();
        String newValue = peopleUtil.ifNumberChangeQty(value);
        Map<String,String> newMap = new HashMap<>();
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
