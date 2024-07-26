package com.apriori.acs.api.tests;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.PropertyValueMap;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.TolerancePolicyDefaultsResponse;
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
public class TolerancePolicyDefaultsTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 10473)
    @Description("Test Get Tolerance Policy Defaults")
    public void testGetTolerancePolicyDefaults() {
        TolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();

        softAssertions.assertThat(propertyValueMap.getTotalRunoutOverride()).isNotNull();
        softAssertions.assertThat(propertyValueMap.getToleranceMode()).containsAnyOf("SYSTEMDEFAULT", "PARTOVERRIDE");
        softAssertions.assertThat(propertyValueMap.isUseCadToleranceThreshhold()).isEqualTo(false);

        GenericExtendedPropertyInfoItem totalRunoutOverrideItem = getTolerancePolicyDefaultsResponse.getPropertyInfoMap().getTotalRunoutOverride();

        softAssertions.assertThat(totalRunoutOverrideItem.getName()).isEqualTo("totalRunoutOverride");
        softAssertions.assertThat(totalRunoutOverrideItem.getUnitTypeName()).containsAnyOf("mm", "in");
        softAssertions.assertThat(totalRunoutOverrideItem.getSupportedSerializedType()).containsAnyOf("DOUBLE", "OBJECT");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10555)
    @Description("Test Error on Get Tolerance Policy Defaults Endpoint")
    public void testErrorOnGetTolerancePolicyDefaultsEndpoint() {
        GenericErrorResponse genericErrorResponse = acsResources.getEndpointInvalidUsername(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS);

        assertOnInvalidResponse(genericErrorResponse);
    }

    @Test
    @TestRail(id = 10556)
    @Description("Test Set Tolerance Policy Defaults")
    public void testSetTolerancePolicyDefaults() {
        double totalRunoutOverrride = 0.1;
        String toleranceMode = "PARTOVERRIDE";
        boolean useCadToleranceThreshold = false;

        GenericResourceCreatedResponse setTolerancePolicyDefaultsResponse = acsResources.setTolerancePolicyDefaults(
            totalRunoutOverrride,
            toleranceMode,
            useCadToleranceThreshold
        );

        softAssertions.assertThat(setTolerancePolicyDefaultsResponse.getResourceCreated()).isEqualTo("false");

        TolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();
        softAssertions.assertThat(propertyValueMap.getTotalRunoutOverride()).isNotNull();
        softAssertions.assertThat(propertyValueMap.getToleranceMode()).isEqualTo("PARTOVERRIDE");
        softAssertions.assertThat(propertyValueMap.isUseCadToleranceThreshhold()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10557)
    @Description("Test Set Tolerance Policy Defaults Invalid User")
    public void testSetGetTolerancePolicyDefaultsInvalidUser() {
        GenericErrorResponse genericErrorResponse = acsResources.setTolerancePolicyDefaultsInvalidUsername();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        softAssertions.assertThat(genericErrorResponse.getErrorCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(genericErrorResponse.getErrorMessage()).isEqualTo("User is not found");
        softAssertions.assertAll();
    }
}
