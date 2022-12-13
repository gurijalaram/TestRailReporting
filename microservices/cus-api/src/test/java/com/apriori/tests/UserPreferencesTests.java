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

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = "16826")
    @Description("Verify GET user preferences endpoint test")
    public void verifyUserPrefTest() {

        PreferenceItemsResponse userPreferencesResponse = new PeopleUtil().getCurrentUserPref(currentUser);

        softAssertions.assertThat(userPreferencesResponse.getItems()).isNotEmpty();
        softAssertions.assertThat(userPreferencesResponse.getPageSize()).isEqualTo(10);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "16828")
    @Description("Verify PATCH(update) user preferences endpoint test")
    public void verifyCurrentUserTest() {
        PeopleUtil peopleUtil = new PeopleUtil();
        PreferenceItemsResponse userPreferencesResponse = peopleUtil
            .getCurrentUserPrefParams(currentUser,new QueryParams().use("pageSize", "100"));
        String identity = getIdentityByParamName(userPreferencesResponse,"display.decimalPlaces");
        String value = getValueByParamName(userPreferencesResponse,"display.decimalPlaces");
        String newValue = peopleUtil.ifNumberChangeQty(value);        //String newValue = peopleUtil.ifNumberChangeQty(value);
        Map<String,String> newMap = new HashMap();
        newMap.put(identity, newValue);

        UpdateUserPrefRequest updateUserPrefRequest =
            UpdateUserPrefRequest.builder().userPreferences(newMap).build();

        peopleUtil.updateCurrentUserPref(currentUser, updateUserPrefRequest);

        PreferenceItemsResponse userPreferencesUpdatedResponse = peopleUtil
            .getCurrentUserPrefParams(currentUser,new QueryParams().use("pageSize", "100"));

        softAssertions.assertThat(userPreferencesUpdatedResponse.getItems()).isNotEmpty();
        softAssertions.assertThat(getValueByParamName(userPreferencesUpdatedResponse,"display.decimalPlaces")).isEqualTo(newValue);
        softAssertions.assertAll();
    }

    private String getIdentityByParamName(PreferenceItemsResponse userPreferencesResponse, String prefName) {
        PreferenceResponse preferenceResponse = userPreferencesResponse.getItems()
            .stream().filter(p -> p.getName().equals(prefName))
            .findFirst()
            .orElse(null);
        return preferenceResponse.getIdentity();
    }

    private String getValueByParamName(PreferenceItemsResponse userPreferencesResponse, String prefName) {
        PreferenceResponse preferenceResponse = userPreferencesResponse.getItems()
            .stream().filter(p -> p.getName().equals(prefName))
            .findFirst()
            .orElse(null);
        return preferenceResponse.getValue();
    }
}
