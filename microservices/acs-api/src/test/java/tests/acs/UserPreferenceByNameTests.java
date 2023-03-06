package tests.acs;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class UserPreferenceByNameTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "10798")
    @Description("Validate Get User Preference By Name Endpoint")
    public void testGetUserPreferenceByName() {
        AcsResources acsResources = new AcsResources();
        String annualVolumeResponse = acsResources.getUserPreferenceByName("prod.info.default.annual.volume");
        String toleranceModeResponse = acsResources.getUserPreferenceByName("TolerancePolicyDefaults.toleranceMode");

        assertThat(annualVolumeResponse, is(equalTo("5500")));
        assertThat(toleranceModeResponse, either(is(containsString("CAD"))).or(is(containsString("PARTOVERRIDE"))).or(is(containsString("SYSTEMDEFAULT"))));
    }

    @Test
    @TestRail(testCaseId = "10846")
    @Description("Validate Get User Preference By Name Endpoint - Negative - Invalid User")
    public void testGetUserPreferenceByNameInvalid() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.getUserPreferenceByNameInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
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
    @TestRail(testCaseId = "10848")
    @Description("Validate Set User Preferences By Name - Negative - Invalid User")
    public void testSetUserPreferenceByNameInvalidUser() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.setUserPreferencesInvalidUser();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(400)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("User is not found")));
    }
}
