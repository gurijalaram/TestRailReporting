package com.apriori.acs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DESIGNER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UserPreferenceByNameTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);

    @AfterEach
    public void cleanup() {
        AcsResources acsResources = new AcsResources(userCredentials);
        acsResources.resetSettings();
        acsResources.resetDisplayUnits();
    }

    @Test
    @TestRail(id = 10798)
    @Description("Validate Get User Preference By Name Endpoint")
    public void testGetUserPreferenceByName() {
        AcsResources acsResources = new AcsResources(userCredentials);
        String annualVolumeResponse = acsResources.getUserPreferenceByName("prod.info.default.annual.volume");
        String toleranceModeResponse = acsResources.getUserPreferenceByName("TolerancePolicyDefaults.toleranceMode");

        assertThat(annualVolumeResponse, is(equalTo("5500")));
        assertThat(toleranceModeResponse, either(is(containsString("CAD"))).or(is(containsString("PARTOVERRIDE"))).or(is(containsString("SYSTEMDEFAULT"))));
    }

    @Test
    @TestRail(id = 10846)
    @Description("Validate Get User Preference By Name Endpoint - Negative - Invalid User")
    public void testGetUserPreferenceByNameInvalid() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.getUserPreferenceByNameInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10847)
    @Description("Validate Set User Preference By Name")
    public void testSetUserPreferenceByName() {
        String useVpeKey = "prod.info.default.use.vpe.for.all.processes";
        String toleranceModeKey = "TolerancePolicyDefaults.toleranceMode";

        AcsResources acsResources = new AcsResources(userCredentials);
        String useVpeCurrentValue = acsResources.getUserPreferenceByName(useVpeKey);
        String toleranceModeCurrentValue = acsResources.getUserPreferenceByName(toleranceModeKey);
        String useVpeValueToSet = useVpeCurrentValue.equals("false") ? "true" : "false";
        String toleranceModeValueToSet = toleranceModeCurrentValue.equals("PARTOVERRIDE") ? "SYSTEMDEFAULT" : "PARTOVERRIDE";

        GenericResourceCreatedResponse setPrefResponseOne = acsResources.setUserPreferenceByName(useVpeKey, useVpeValueToSet);
        GenericResourceCreatedResponse setPrefResponseTwo = acsResources.setUserPreferenceByName(toleranceModeKey, toleranceModeValueToSet);

        assertThat(setPrefResponseOne.getResourceCreated(), is(equalTo("false")));
        assertThat(setPrefResponseTwo.getResourceCreated(), is(equalTo("false")));

        assertThat(acsResources.getUserPreferenceByName(useVpeKey), is(containsString(useVpeValueToSet)));
        assertThat(acsResources.getUserPreferenceByName(toleranceModeKey), is(containsString(toleranceModeValueToSet)));
    }

    @Test
    @TestRail(id = 10848)
    @Description("Validate Set User Preferences By Name - Negative - Invalid User")
    public void testSetUserPreferenceByNameInvalidUser() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.setUserPreferencesInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(HttpStatus.SC_BAD_REQUEST)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("User is not found")));
    }
}
