package com.apriori.acs.api.tests;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.userpreferences.UserPreferencesResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.restassured.http.Header;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UserPreferencesTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void cleanup() {
        acsResources.resetSettings();
        acsResources.resetDisplayUnits();
    }

    @Test
    @TestRail(id = 10759)
    @Description("Verify Get User Preferences Endpoint")
    public void testGetUserPreferencesEndpoint() {
        UserPreferencesResponse getUserPreferencesResponse = acsResources.getUserPreferences();

        softAssertions.assertThat(getUserPreferencesResponse.getCostTableDecimalPlaces()).containsAnyOf("3", "2");
        softAssertions.assertThat(getUserPreferencesResponse.getDefaultScenarioName()).isEqualTo("Initial");
        softAssertions.assertThat(getUserPreferencesResponse.getProdInfoDefaultAnnualVolume()).isEqualTo("5500");
        softAssertions.assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsToleranceMode())
            .containsAnyOf("CAD", "PARTOVERRIDE", "SYSTEMDEFAULT");
        softAssertions.assertThat(getUserPreferencesResponse.getTolerancePolicyDefaultsUseCadToleranceThreshhold()).isEqualTo("false");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10796)
    @Description("Get User Preferences Negative Test - Invalid Username")
    public void testGetUserPreferencesInvalidUser() {
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.USER_PREFERENCES);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10842)
    @Description("Validate Set User Preferences Endpoint")
    public void testSetUserPreferencesEndpoint() {
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

        softAssertions.assertThat(genericResourceCreatedResponse.getResourceCreated()).isEqualTo("false");

        UserPreferencesResponse getUserPreferencesResponsePostChanges = acsResources.getUserPreferences();

        softAssertions.assertThat(getUserPreferencesResponsePostChanges.getCostTableDecimalPlaces()).isEqualTo(costTableDecimalPlacesValueToSet);
        softAssertions.assertThat(getUserPreferencesResponsePostChanges.getProdInfoDefaultUseVpeForAllProcesses()).isEqualTo(useVpeForAllProcessValueToSet);
        softAssertions.assertThat(getUserPreferencesResponsePostChanges.getTolerancePolicyDefaultsToleranceMode()).isEqualTo(toleranceModeValueToSet);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10844)
    @Description("Validate Set User Preferences Endpoint - Negative - Invalid User")
    public void testSetUserPreferencesEndpointInvalidUser() {
        GenericErrorResponse genericErrorResponse = acsResources.setUserPreferencesInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 21727)
    @Description("Verify that header -apriori-version is returned")
    public void testCorrectHeaderIsReturned() {
        Header header = acsResources.getUserPreferencesHeaders()
            .get("X-aPriori-Version");

        softAssertions.assertThat(header).isNotNull();
        softAssertions.assertThat(header.getName()).isEqualTo("X-aPriori-Version");
        softAssertions.assertAll();
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(genericErrorResponse.getErrorMessage()).isEqualTo("User is not found");
        softAssertions.assertAll();
    }
}
