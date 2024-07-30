package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UserPreferenceByNameTests extends TestUtil {
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
    @TestRail(id = 10798)
    @Description("Validate Get User Preference By Name Endpoint")
    public void testGetUserPreferenceByName() {
        String annualVolumeResponse = acsResources.getUserPreferenceByName("prod.info.default.annual.volume");
        String toleranceModeResponse = acsResources.getUserPreferenceByName("TolerancePolicyDefaults.toleranceMode");

        softAssertions.assertThat(annualVolumeResponse).isEqualTo("5500");
        softAssertions.assertThat(toleranceModeResponse).containsAnyOf("CAD", "PARTOVERRIDE", "SYSTEMDEFAULT");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10846)
    @Description("Validate Get User Preference By Name Endpoint - Negative - Invalid User")
    public void testGetUserPreferenceByNameInvalid() {
        GenericErrorResponse genericErrorResponse = acsResources.getUserPreferenceByNameInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10847)
    @Description("Validate Set User Preference By Name")
    public void testSetUserPreferenceByName() {
        String useVpeKey = "prod.info.default.use.vpe.for.all.processes";
        String toleranceModeKey = "TolerancePolicyDefaults.toleranceMode";

        String useVpeCurrentValue = acsResources.getUserPreferenceByName(useVpeKey);
        String toleranceModeCurrentValue = acsResources.getUserPreferenceByName(toleranceModeKey);
        String useVpeValueToSet = useVpeCurrentValue.equals("false") ? "true" : "false";
        String toleranceModeValueToSet = toleranceModeCurrentValue.equals("PARTOVERRIDE") ? "SYSTEMDEFAULT" : "PARTOVERRIDE";

        GenericResourceCreatedResponse setPrefResponseOne = acsResources.setUserPreferenceByName(useVpeKey, useVpeValueToSet);
        GenericResourceCreatedResponse setPrefResponseTwo = acsResources.setUserPreferenceByName(toleranceModeKey, toleranceModeValueToSet);

        softAssertions.assertThat(setPrefResponseOne.getResourceCreated()).isEqualTo("false");
        softAssertions.assertThat(setPrefResponseTwo.getResourceCreated()).isEqualTo("false");

        softAssertions.assertThat(acsResources.getUserPreferenceByName(useVpeKey)).contains(useVpeValueToSet);
        softAssertions.assertThat(acsResources.getUserPreferenceByName(toleranceModeKey)).contains(toleranceModeValueToSet);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10848)
    @Description("Validate Set User Preferences By Name - Negative - Invalid User")
    public void testSetUserPreferenceByNameInvalidUser() {
        GenericErrorResponse genericErrorResponse = acsResources.setUserPreferencesInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(genericErrorResponse.getErrorMessage()).isEqualTo("User is not found");
        softAssertions.assertAll();
    }
}
