package com.apriori.acs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DESIGNER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.userpreferences.UserPreferencesResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.restassured.http.Header;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UserPreferencesTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);

    @AfterEach
    public void cleanup() {
        AcsResources acsResources = new AcsResources(userCredentials);
        acsResources.resetSettings();
        acsResources.resetDisplayUnits();
    }

    @Test
    @TestRail(id = 10759)
    @Description("Verify Get User Preferences Endpoint")
    public void testGetUserPreferencesEndpoint() {
        AcsResources acsResources = new AcsResources(userCredentials);
        UserPreferencesResponse getUserPreferencesResponse = acsResources.getUserPreferences();

        assertThat(getUserPreferencesResponse.getCostTableDecimalPlaces(), either(is("3")).or(is("2")));
        assertThat(getUserPreferencesResponse.getDefaultScenarioName(), is(equalTo("Initial")));
        assertThat(getUserPreferencesResponse.getProdInfoDefaultAnnualVolume(), is(equalTo("5500")));
        assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsToleranceMode(),
            either(containsString("CAD")).or(is(containsString("PARTOVERRIDE"))).or(is("SYSTEMDEFAULT")));
        assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsUseCadToleranceThreshhold(), is(equalTo("false")));
    }

    @Test
    @TestRail(id = 10796)
    @Description("Get User Preferences Negative Test - Invalid Username")
    public void testGetUserPreferencesInvalidUser() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.USER_PREFERENCES);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10842)
    @Description("Validate Set User Preferences Endpoint")
    public void testSetUserPreferencesEndpoint() {
        AcsResources acsResources = new AcsResources(userCredentials);
        UserPreferencesResponse getUserPreferencesResponse = acsResources.getUserPreferences();
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

        UserPreferencesResponse getUserPreferencesResponsePostChanges = acsResources.getUserPreferences();

        assertThat(getUserPreferencesResponsePostChanges.getCostTableDecimalPlaces(), is(equalTo(costTableDecimalPlacesValueToSet)));
        assertThat(getUserPreferencesResponsePostChanges.getProdInfoDefaultUseVpeForAllProcesses(), is(equalTo(useVpeForAllProcessValueToSet)));
        assertThat(getUserPreferencesResponsePostChanges.getTolerancePolicyDefaultsToleranceMode(), is(equalTo(toleranceModeValueToSet)));
    }

    @Test
    @TestRail(id = 10844)
    @Description("Validate Set User Preferences Endpoint - Negative - Invalid User")
    public void testSetUserPreferencesEndpointInvalidUser() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.setUserPreferencesInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 21727)
    @Description("Verify that header -apriori-version is returned")
    public void testCorrectHeaderIsReturned() {
        AcsResources acsResources = new AcsResources(userCredentials);
        Header header = acsResources.getUserPreferencesHeaders()
            .get("X-aPriori-Version");

        assertThat(header.getName(), equalTo("X-aPriori-Version"));
        assertThat(header.getValue(), notNullValue());
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(HttpStatus.SC_BAD_REQUEST)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("User is not found")));
    }
}
