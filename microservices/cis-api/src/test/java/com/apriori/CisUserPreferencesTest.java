package com.apriori;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.cis.controller.CisUserPreferencesResources;
import com.apriori.cis.models.response.userpreferences.CurrentExtendedUserPreferencesResponse;
import com.apriori.cis.models.response.userpreferences.ExtendedUserPreferencesResponse;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CisUserPreferencesTest extends CisUserPreferencesResources {

    @BeforeEach
    public void testSetup() {
        RequestEntityUtil.useTokenForRequests(UserUtil.getUser().getToken());
    }

    @Test
    @TestRail(id = 22095)
    @Description("Get Extended User Preferences")
    public void testGetCurrentExtendedUserPreferences() {
        CurrentExtendedUserPreferencesResponse currentExtendedUserPreferenceResponse = getCurrentExtendedUserPreferences();
        assertThat(currentExtendedUserPreferenceResponse.getIdentity(), is(notNullValue()));
    }

    @Test
    @TestRail(id = 22096)
    @Description("Get Logged in user's Extended User Preferences")
    public void testGetLoggedExtendedUserPreferences() {
        ExtendedUserPreferencesResponse loggedExtendedUserPreferenceResponse = getLoggedExtendedUserPreferences();
        assertThat(loggedExtendedUserPreferenceResponse.getItems().size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(id = 22097)
    @Description("Update Extended User Preferences")
    public void testUpdateCurrentExtendedUserPreferences() {
        String avatarColor = "#009999";
        CurrentExtendedUserPreferencesResponse loggedExtendedUserPreferenceResponse = updateCurrentExtendedUserPreferences(avatarColor);
        assertThat(loggedExtendedUserPreferenceResponse.getAvatarColor(), is(equalTo(avatarColor)));
    }
}
