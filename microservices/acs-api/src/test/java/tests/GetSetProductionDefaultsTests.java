package tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
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
        GetProductionDefaultsResponse getProductionDefaultsResponse = acsResources
                .getProductionDefaults(UserUtil.getUser().getUsername());

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
        String invalidUserResponse = acsResources.getProductionDefaultsInvalidUsername(
                UserUtil.getUser().getUsername().substring(0, 14).concat("41"));

        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10588")
    @Description("Verify Set Production Defaults Endpoint")
    public void testSetProductionDetailsEndpoint() {
        AcsResources acsResources = new AcsResources();
        String username = UserUtil.getUser().getUsername();

        GenericResourceCreatedResponse setProductionDefaultsResponse = acsResources
                .setProductionDefaults(username);

        assertThat(setProductionDefaultsResponse.getResourceCreated(), is(equalTo("false")));

        GetProductionDefaultsResponse getProductionDefaultsResponse = acsResources
                .getProductionDefaults(username);

        assertThat(getProductionDefaultsResponse.getPg(), is(equalTo(null)));
        assertThat(getProductionDefaultsResponse.getVpe(), is(equalTo(null)));
        assertThat(getProductionDefaultsResponse.getMaterial(), is(equalTo("Accura 10")));
        assertThat(getProductionDefaultsResponse.getMaterialCatalogName(), is(equalTo(null)));
        assertThat(getProductionDefaultsResponse.getAnnualVolume(), is(equalTo("5500")));
        assertThat(getProductionDefaultsResponse.getProductionLife(), is(equalTo(5.0)));
        assertThat(getProductionDefaultsResponse.getBatchSize(), is(equalTo(458)));
        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), is(equalTo(false)));
        //assertThat(getProductionDefaultsResponse.getDefaults(), is(equalTo(null)));
        assertThat(getProductionDefaultsResponse.isBatchSizeMode(), is(equalTo(false)));
    }
}
