package tests.acs;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.enums.acs.AcsApiEnum;
import com.apriori.acs.entity.response.acs.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.acs.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetProductionDefaultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10586")
    @Description("Verify Get Production Defaults Endpoint")
    public void testGetProductionDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources();
        GetProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        assertThat(getProductionDefaultsResponse.getMaterial(), anyOf(equalTo("Accura 10"), equalTo("Use Default")));
        assertThat(getProductionDefaultsResponse.getAnnualVolume(), is(equalTo("5500")));
        assertThat(getProductionDefaultsResponse.getProductionLife(), is(equalTo(5.0)));
        assertThat(getProductionDefaultsResponse.getBatchSize(), is(equalTo(458)));
        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), is(equalTo(false)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10587")
    @Description("Verify Get Production Defaults Endpoint - Negative Test")
    public void testGetProductionDefaultsEndpointInvalidUser() {
        AcsResources acsResources = new AcsResources();
        String invalidUserResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS);

        assertInvalidResponse(invalidUserResponse);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10588")
    @Description("Verify Set Production Defaults Endpoint")
    public void testSetProductionDetailsEndpoint() {
        AcsResources acsResources = new AcsResources();

        GenericResourceCreatedResponse setProductionDefaultsResponse = acsResources.setProductionDefaults();

        assertThat(setProductionDefaultsResponse.getResourceCreated(), is(equalTo("false")));

        GetProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        assertThat(getProductionDefaultsResponse.getMaterial(), is(equalTo("Accura 10")));
        assertThat(getProductionDefaultsResponse.getAnnualVolume(), is(equalTo("5500")));
        assertThat(getProductionDefaultsResponse.getProductionLife(), is(equalTo(5.0)));
        assertThat(getProductionDefaultsResponse.getBatchSize(), is(equalTo(458)));
        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), is(equalTo(false)));
        assertThat(getProductionDefaultsResponse.isBatchSizeMode(), is(equalTo(false)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10606")
    @Description("Set Production Defaults Negative Test")
    public void testNegativeSetProductionDefaults() {
        AcsResources acsResources = new AcsResources();
        String invalidUserResponse = acsResources.setProductionDefaultsInvalidUsername();

        assertInvalidResponse(invalidUserResponse);
    }

    private void assertInvalidResponse(String invalidUserResponse) {
        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }
}
