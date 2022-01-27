package tests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.enums.AcsApiEnum;
import com.apriori.acs.entity.response.getsetuserpreferences.GetUserPreferencesResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetUserPreferencesTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10759")
    @Description("Verify Get User Preferences Endpoint")
    public void testGetUserPreferencesEndpoint() {
        AcsResources acsResources = new AcsResources();
        GetUserPreferencesResponse getUserPreferencesResponse = acsResources.getUserPreferences();

        assertThat(getUserPreferencesResponse.getCostTableDecimalPlaces(), is(equalTo("2")));
        assertThat(getUserPreferencesResponse.getDefaultScenarioName(), is(equalTo("Initial")));
        assertThat(getUserPreferencesResponse.getProdInfoDefaultMaterial(), anyOf(equalTo("Use Default"), equalTo("Accura 10")));
        assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsToleranceMode(), is(equalTo("SYSTEMDEFAULT")));
        assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsUseCadToleranceThreshhold(), is(equalTo("false")));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10796")
    @Description("Get User Preferences Negative Test - Invalid Username")
    public void testGetUserPreferencesInvalidUser() {
        AcsResources acsResources = new AcsResources();
        String errorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.GET_SET_USER_PREFERENCES);

        assertInvalidResponse(errorResponse);
    }

    private void assertInvalidResponse(String invalidUserResponse) {
        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }
}
