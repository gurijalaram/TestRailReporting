package tests.acs;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.enums.acs.AcsApiEnum;
import com.apriori.acs.entity.response.acs.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.acs.getsetuserpreferences.GetUserPreferencesResponse;
import com.apriori.acs.utils.acs.AcsResources;
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

        assertThat(getUserPreferencesResponse.getCostTableDecimalPlaces(), anyOf(equalTo("3"), equalTo("2")));
        assertThat(getUserPreferencesResponse.getDefaultScenarioName(), is(equalTo("Initial")));
        assertThat(getUserPreferencesResponse.getProdInfoDefaultAnnualVolume(), is(equalTo("5500")));
        assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsToleranceMode(), anyOf(containsString("CAD"),
            containsString("PARTOVERRIDE"), containsString("SYSTEMDEFAULT")));
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

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10842")
    @Description("Validate Set User Preferences Endpoint")
    public void testSetUserPreferencesEndpoint() {
        AcsResources acsResources = new AcsResources();
        GetUserPreferencesResponse getUserPreferencesResponse = acsResources.getUserPreferences();
        String costTableDecimalPlaces = getUserPreferencesResponse.getCostTableDecimalPlaces();
        String useVpeForAllProcesses = getUserPreferencesResponse.getProdInfoDefaultUseVpeForAllProcesses();
        String toleranceMode = getUserPreferencesResponse.getTolerancePolicyDefaultsToleranceMode();

        String costTableDecimalPlacesValueToSet = costTableDecimalPlaces.equals("2") ? "3" : "2";
        String useVpeForAllProcessValueToSet = useVpeForAllProcesses.equals("false") ? "true" : "false";
        String toleranceModeValueToSet = toleranceMode.equals("PARTOVERRIDE") ? "SYSTEMDEFAULT" : "PARTOVERRIDE";

        GenericResourceCreatedResponse genericResourceCreatedResponse = acsResources.setUserPreferences(
            costTableDecimalPlacesValueToSet,
            useVpeForAllProcessValueToSet,
            toleranceModeValueToSet
        );

        assertThat(genericResourceCreatedResponse.getResourceCreated(), is(equalTo("false")));

        GetUserPreferencesResponse getUserPreferencesResponsePostChanges = acsResources.getUserPreferences();

        assertThat(getUserPreferencesResponsePostChanges.getCostTableDecimalPlaces(), is(equalTo(costTableDecimalPlacesValueToSet)));
        assertThat(getUserPreferencesResponsePostChanges.getProdInfoDefaultUseVpeForAllProcesses(), is(equalTo(useVpeForAllProcessValueToSet)));
        assertThat(getUserPreferencesResponsePostChanges.getTolerancePolicyDefaultsToleranceMode(), is(equalTo(toleranceModeValueToSet)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10844")
    @Description("Validate Set User Preferences Endpoint - Negative - Invalid User")
    public void testSetUserPreferencesEndpointInvalidUser() {
        AcsResources acsResources = new AcsResources();
        String invalidUserResponse = acsResources.setUserPreferencesInvalidUser();

        assertInvalidResponse(invalidUserResponse);
    }

    private void assertInvalidResponse(String invalidUserResponse) {
        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }
}