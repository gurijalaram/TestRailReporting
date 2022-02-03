package tests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.PropertyInfoItem;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.PropertyValueMap;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetTolerancePolicyDefaultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10473")
    @Description("Test Get Tolerance Policy Defaults")
    public void testGetTolerancePolicyDefaults() {
        AcsResources acsResources = new AcsResources();
        GetTolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();

        assertThat(propertyValueMap.getTotalRunoutOverride(), is(notNullValue()));
        assertThat(propertyValueMap.getToleranceMode(), anyOf(equalTo("SYSTEMDEFAULT"), equalTo("PARTOVERRIDE")));
        assertThat(propertyValueMap.isUseCadToleranceThreshhold(), is(equalTo(false)));

        PropertyInfoItem totalRunoutOverrideItem = getTolerancePolicyDefaultsResponse.getPropertyInfoMap().getTotalRunoutOverride();

        assertThat(totalRunoutOverrideItem.getName(), is(equalTo("totalRunoutOverride")));
        assertThat(totalRunoutOverrideItem.getUnitTypeName(), anyOf(equalTo("mm"), equalTo("in")));
        assertThat(totalRunoutOverrideItem.getSupportedSerializedType(), is(equalTo("DOUBLE")));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10555")
    @Description("Test Error on Get Tolerance Policy Defaults Endpoint")
    public void testErrorOnGetTolerancePolicyDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources();
        String http400ErrorResponse = acsResources.getTolerancePolicyDefaults400Error();

        assertInvalidResponse(http400ErrorResponse);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10556")
    @Description("Test Set Tolerance Policy Defaults")
    public void testSetTolerancePolicyDefaults() {
        double totalRunoutOverrride = 0.1;
        String toleranceMode = "PARTOVERRIDE";
        boolean useCadToleranceThreshold = false;

        AcsResources acsResources = new AcsResources();
        GenericResourceCreatedResponse setTolerancePolicyDefaultsResponse = acsResources.setTolerancePolicyDefaults(
                totalRunoutOverrride,
                toleranceMode,
                useCadToleranceThreshold
        );

        assertThat(setTolerancePolicyDefaultsResponse.getResourceCreated(), is(equalTo("false")));

        GetTolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources.getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();
        assertThat(propertyValueMap.getTotalRunoutOverride(), is(notNullValue()));
        assertThat(propertyValueMap.getToleranceMode(), is(equalTo("PARTOVERRIDE")));
        assertThat(propertyValueMap.isUseCadToleranceThreshhold(), is(equalTo(false)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10557")
    @Description("Test Set Tolerance Policy Defaults Invalid User")
    public void testSetGetTolerancePolicyDefaultsInvalidUser() {
        AcsResources acsResources = new AcsResources();
        String http400ErrorResponse = acsResources.setTolerancePolicyDefaultsInvalidUsername(
                0.1,
                "PARTOVERRIDE",
                false
        );

        assertInvalidResponse(http400ErrorResponse);
    }

    private void assertInvalidResponse(String invalidUserResponse) {
        assertThat(invalidUserResponse, is(containsString("400")));
        assertThat(invalidUserResponse, is(containsString("User is not found")));
    }
}
