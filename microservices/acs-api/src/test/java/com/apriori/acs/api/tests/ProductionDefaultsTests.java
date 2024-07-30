package com.apriori.acs.api.tests;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.productiondefaults.ProductionDefaultsResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ProductionDefaultsTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 10586)
    @Description("Verify Get Production Defaults Endpoint")
    public void testGetProductionDefaultsEndpoint() {
        ProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        softAssertions.assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses()).isIn(true, false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10587)
    @Description("Verify Get Production Defaults Endpoint - Negative Test")
    public void testGetProductionDefaultsEndpointInvalidUser() {
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.PRODUCTION_DEFAULTS);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10588)
    @Description("Verify Set Production Defaults Endpoint")
    public void testSetProductionDetailsEndpoint() {
        GenericResourceCreatedResponse setProductionDefaultsResponse = acsResources.setProductionDefaults();

        softAssertions.assertThat(setProductionDefaultsResponse.getResourceCreated()).isEqualTo("false");

        ProductionDefaultsResponse getProductionDefaultsResponse = acsResources.getProductionDefaults();

        softAssertions.assertThat(getProductionDefaultsResponse.getMaterial()).isEqualTo("Accura 10");
        softAssertions.assertThat(getProductionDefaultsResponse.getAnnualVolume()).isEqualTo("5500");
        softAssertions.assertThat(getProductionDefaultsResponse.getProductionLife()).isEqualTo(5.0);
        softAssertions.assertThat(getProductionDefaultsResponse.getBatchSize()).isEqualTo(458);
        softAssertions.assertThat(getProductionDefaultsResponse.isUseVpeForAllProcesses()).isEqualTo(false);
        softAssertions.assertThat(getProductionDefaultsResponse.isBatchSizeMode()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10606)
    @Description("Set Production Defaults Negative Test")
    public void testNegativeSetProductionDefaults() {
        GenericErrorResponse genericErrorResponse = acsResources.setProductionDefaultsInvalidUsername();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(genericErrorResponse.getErrorMessage()).isEqualTo("User is not found");
    }
}
