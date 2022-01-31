package tests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetUserPreferenceByNameTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10798")
    @Description("Validate Get User Preference By Name Endpoint")
    public void testGetUserPreferenceByName() {
        AcsResources acsResources = new AcsResources();
        String annualVolumeResponse = acsResources.getUserPreferenceByName("prod.info.default.annual.volume");
        String toleranceModeResponse = acsResources.getUserPreferenceByName("TolerancePolicyDefaults.toleranceMode");

        assertThat(annualVolumeResponse, is(equalTo("5500")));
        assertThat(toleranceModeResponse, anyOf(equalTo("PARTOVERRIDE"), equalTo("SYSTEMDEFAULT")));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10846")
    @Description("Validate Get User Preference By Name Endpoint - Negative - Invalid User")
    public void testGetUserPreferenceByNameInvalid() {
        AcsResources acsResources = new AcsResources();
        String errorResponse = acsResources.getUserPreferenceByNameInvalidUser();

        assertInvalidResponse(errorResponse);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10847")
    @Description("Validate Set User Preference By Name")
    public void testSetUserPreferenceByName() {
        String useVpeKey = "prod.info.default.use.vpe.for.all.processes";
        String toleranceModeKey = "TolerancePolicyDefaults.toleranceMode";

        AcsResources acsResources = new AcsResources();
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10848")
    @Description("Validate Set User Preferences By Name - Negative - Invalid User")
    public void testSetUserPreferenceByNameInvalidUser() {
        AcsResources acsResources = new AcsResources();
        String setUserPrefInvalidResponse = acsResources.setUserPreferencesInvalidUser();

        assertInvalidResponse(setUserPrefInvalidResponse);
    }

    private void assertInvalidResponse(String invalidUserResponse) {
        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }
}
