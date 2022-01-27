package tests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetUserPreferencesByNameTests {

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
}
