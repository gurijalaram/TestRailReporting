package com.apriori.cis.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.cisapi.controller.CisUserPreferencesResources;
import com.apriori.cisapi.entity.response.userpreferences.CurrentExtendedUserPreferencesResponse;
import com.apriori.cisapi.entity.response.userpreferences.ExtendedUserPreferencesResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;


public class CisUserPreferencesTest extends CisUserPreferencesResources {

    @BeforeClass
    public static void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = "22095")
    @Description("Get Extended User Preferences")
    public void testGetCurrentExtendedUserPreferences() {
        CurrentExtendedUserPreferencesResponse currentExtendedUserPreferenceResponse = getCurrentExtendedUserPreferences();
        assertThat(currentExtendedUserPreferenceResponse.getIdentity(), is(notNullValue()));
    }

    @Test
    @TestRail(testCaseId = "22096")
    @Description("Get Logged in user's Extended User Preferences")
    public void testGetLoggedExtendedUserPreferences() {
        ExtendedUserPreferencesResponse loggedExtendedUserPreferenceResponse = getLoggedExtendedUserPreferences();
        assertThat(loggedExtendedUserPreferenceResponse.getItems().size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "22097")
    @Description("Update Extended User Preferences")
    public void testUpdateCurrentExtendedUserPreferences() {
        String avatarColor = "#009999";
        CurrentExtendedUserPreferencesResponse loggedExtendedUserPreferenceResponse = updateCurrentExtendedUserPreferences(avatarColor);
        assertThat(loggedExtendedUserPreferenceResponse.getAvatarColor(), is(equalTo(avatarColor)));
    }
}
