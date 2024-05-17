package com.apriori.acs.api.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericExtendedPropertyInfoItem;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.PropertyValueMap;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.TolerancePolicyDefaultsResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class TolerancePolicyDefaultsTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);

    @Test
    @TestRail(id = 10473)
    @Description("Test Get Tolerance Policy Defaults")
    public void testGetTolerancePolicyDefaults() {
        AcsResources acsResources = new AcsResources(userCredentials);
        TolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();

        assertThat(propertyValueMap.getTotalRunoutOverride(), is(notNullValue()));
        assertThat(propertyValueMap.getToleranceMode(), anyOf((equalTo("SYSTEMDEFAULT")), equalTo("PARTOVERRIDE")));
        assertThat(propertyValueMap.isUseCadToleranceThreshhold(), is(equalTo(false)));

        GenericExtendedPropertyInfoItem totalRunoutOverrideItem = getTolerancePolicyDefaultsResponse.getPropertyInfoMap().getTotalRunoutOverride();

        assertThat(totalRunoutOverrideItem.getName(), is(equalTo("totalRunoutOverride")));
        assertThat(totalRunoutOverrideItem.getUnitTypeName(), anyOf(equalTo("mm"), equalTo("in")));
        assertThat(totalRunoutOverrideItem.getSupportedSerializedType(), anyOf(equalTo("DOUBLE"), equalTo("OBJECT")));
    }

    @Test
    @TestRail(id = 10555)
    @Description("Test Error on Get Tolerance Policy Defaults Endpoint")
    public void testErrorOnGetTolerancePolicyDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources(userCredentials);
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

        AcsResources acsResources = new AcsResources(userCredentials);
        GenericResourceCreatedResponse setTolerancePolicyDefaultsResponse = acsResources.setTolerancePolicyDefaults(
            totalRunoutOverrride,
            toleranceMode,
            useCadToleranceThreshold
        );

        assertThat(setTolerancePolicyDefaultsResponse.getResourceCreated(), is(equalTo("false")));

        TolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();
        assertThat(propertyValueMap.getTotalRunoutOverride(), is(notNullValue()));
        assertThat(propertyValueMap.getToleranceMode(), is(equalTo("PARTOVERRIDE")));
        assertThat(propertyValueMap.isUseCadToleranceThreshhold(), is(equalTo(false)));
    }

    @Test
    @TestRail(id = 10557)
    @Description("Test Set Tolerance Policy Defaults Invalid User")
    public void testSetGetTolerancePolicyDefaultsInvalidUser() {
        AcsResources acsResources = new AcsResources(userCredentials);
        GenericErrorResponse genericErrorResponse = acsResources.setTolerancePolicyDefaultsInvalidUsername();

        assertOnInvalidResponse(genericErrorResponse);
    }

    private void assertOnInvalidResponse(GenericErrorResponse genericErrorResponse) {
        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(HttpStatus.SC_BAD_REQUEST)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("User is not found")));
    }
}
