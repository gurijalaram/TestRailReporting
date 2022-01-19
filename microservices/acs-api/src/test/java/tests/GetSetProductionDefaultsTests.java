package tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import io.qameta.allure.Description;

import org.assertj.core.api.SoftAssertions;
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

        assertThat(getProductionDefaultsResponse.getMaterial(), is(equalTo("Accura 10")));
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
        String invalidUserResponse = acsResources.getProductionDefaultsInvalidUsername();

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

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(getProductionDefaultsResponse.getMaterial().equals("Accura 10"));
        softAssertions.assertThat(getProductionDefaultsResponse.getAnnualVolume().equals("5500"));
        softAssertions.assertThat(getProductionDefaultsResponse.getProductionLife() == 5.0);
        softAssertions.assertThat(getProductionDefaultsResponse.getBatchSize() == 458);
        softAssertions.assertThat(!getProductionDefaultsResponse.isUseVpeForAllProcesses());
        softAssertions.assertThat(!getProductionDefaultsResponse.isBatchSizeMode());

        softAssertions.assertAll();
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
