package tests.acs;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.enums.acs.AcsApiEnum;
import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.acs.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetProductionDefaultsTests extends TestUtil {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10586")
    @Description("Verify Get Production Defaults Endpoint")
    public void testGetProductionDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources();
        GetProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), anyOf(equalTo(true), equalTo(false)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10587")
    @Description("Verify Get Production Defaults Endpoint - Negative Test")
    public void testGetProductionDefaultsEndpointInvalidUser() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.PRODUCTION_DEFAULTS);

        assertOnInvalidResponse(genericErrorResponse);
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
        GenericErrorResponse genericErrorResponse = acsResources.setProductionDefaultsInvalidUsername();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(400)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("User is not found")));
    }
}
