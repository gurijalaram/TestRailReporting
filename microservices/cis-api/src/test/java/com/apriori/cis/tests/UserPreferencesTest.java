package com.apriori.cis.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cisapi.entity.response.user.preferences.UserPreferencesResponse;
import com.apriori.cisapi.utils.UserPreferencesController;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class UserPreferencesTest extends UserPreferencesController {

    @BeforeClass
    public static void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = "9798")
    @Description("Get User Preferences call to CIS API")
    public void testGetUserPreferences() {
        List<UserPreferencesResponse> userPreferenceItems = getUserPreferences();
        assertThat(userPreferenceItems.size(), is(greaterThan(0)));
    }
}
