package tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.PropertyInfoItem;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.PropertyValueMap;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matcher;
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
        GetTolerancePolicyDefaultsResponse getTolerancePolicyDefaultsResponse = acsResources
                .getTolerancePolicyDefaults();

        PropertyValueMap propertyValueMap = getTolerancePolicyDefaultsResponse.getPropertyValueMap();
        assertThat(propertyValueMap.getTotalRunoutOverride(), is(equalTo(1.4)));
        assertThat(propertyValueMap.getToleranceMode(), is(equalTo("SYSTEMDEFAULT")));
        assertThat(propertyValueMap.isUseCadToleranceThreshhold(), is(equalTo(false)));

        PropertyInfoItem totalRunoutOverrideItem = getTolerancePolicyDefaultsResponse.getPropertyInfoMap()
                .getTotalRunoutOverride();
        assertThat(totalRunoutOverrideItem.getName(), is(equalTo("totalRunoutOverride")));
        assertThat(totalRunoutOverrideItem.getUnitTypeName(), is(equalTo("mm")));
        assertThat(totalRunoutOverrideItem.getSupportedSerializedType(), is(equalTo("DOUBLE")));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10555")
    @Description("Test Error on Get Tolerance Policy Defaults Endpoint")
    public void testErrorOnGetTolerancePolicyDefaultsEndpoint() {
        AcsResources acsResources = new AcsResources();
        String http400ErrorResponse = acsResources
                .getTolerancePolicyDefaults400Error(UserUtil.getUser().getUsername().substring(0, 14).concat("41"));

        assertThat(http400ErrorResponse, is(containsString("400")));
        assertThat(http400ErrorResponse, is(containsString("User is not found")));
    }
}
