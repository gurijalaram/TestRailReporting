package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.acs.enums.acs.AcsApiEnum;
import com.apriori.acs.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.models.response.acs.productiondefaults.ProductionDefaultsResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.http.utils.TestUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ProductionDefaultsTests extends TestUtil {

    @Test
    @TestRail(id = 10586)
    @Description("Verify Get Production Defaults Endpoint")
    public void testGetProductionDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources();
        ProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), anyOf(equalTo(true), equalTo(false)));
    }

    @Test
    @TestRail(id = 10587)
    @Description("Verify Get Production Defaults Endpoint - Negative Test")
    public void testGetProductionDefaultsEndpointInvalidUser() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.PRODUCTION_DEFAULTS);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10588)
    @Description("Verify Set Production Defaults Endpoint")
    public void testSetProductionDetailsEndpoint() {
        AcsResources acsResources = new AcsResources();

        GenericResourceCreatedResponse setProductionDefaultsResponse = acsResources.setProductionDefaults();

        assertThat(setProductionDefaultsResponse.getResourceCreated(), is(equalTo("false")));

        ProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        assertThat(getProductionDefaultsResponse.getMaterial(), is(equalTo("Accura 10")));
        assertThat(getProductionDefaultsResponse.getAnnualVolume(), is(equalTo("5500")));
        assertThat(getProductionDefaultsResponse.getProductionLife(), is(equalTo(5.0)));
        assertThat(getProductionDefaultsResponse.getBatchSize(), is(equalTo(458)));
        assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses(), is(equalTo(false)));
        assertThat(getProductionDefaultsResponse.isBatchSizeMode(), is(equalTo(false)));
    }

    @Test
    @TestRail(id = 10606)
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
