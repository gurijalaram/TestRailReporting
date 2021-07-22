package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getunitvariantsettings.GetCustomUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;

import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;

public class GetUnitVariantSettingsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8772")
    @Description("Test Get Unit Variant Settings")
    public void testGetUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        GetUnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        assertThat(getUnitVariantSettingsResponse, is(notNullValue()));

        ArrayList<UnitVariantSetting> allItems = getUnitVariantSettingsResponse.getAllUnitVariantSetting();
        assertThat(allItems, is(notNullValue()));

        for (UnitVariantSetting item : allItems) {
            assertThat(item.getType(), is(equalTo("simple")));
            assertThat(item.getDecimalPlaces(), is(equalTo(2.0)));
            assertThat(item.isSystem(), is(equalTo(true)));
            assertThat(item.isCustom(), is(equalTo(false)));
        }
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8773")
    @Description("Test Get Custom Unit Variant Settings")
    public void testGetCustomUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        GetCustomUnitVariantSettingsResponse getCustomUnitVariantSettingsResponse =
                acsResources.getCustomUnitVariantSettings();

        assertThat(getCustomUnitVariantSettingsResponse, is(notNullValue()));
        assertThat(getCustomUnitVariantSettingsResponse.getType(), is(equalTo("simple")));
        assertThat(getCustomUnitVariantSettingsResponse.getName(), is(equalTo("CUSTOM")));
        assertThat(getCustomUnitVariantSettingsResponse.getMetric(), is(equalTo("true")));
        assertThat(getCustomUnitVariantSettingsResponse.getLength(), is(equalTo("m")));
        assertThat(getCustomUnitVariantSettingsResponse.getMass(), is(equalTo("g")));
        assertThat(getCustomUnitVariantSettingsResponse.getTime(), is(equalTo("s")));
        assertThat(getCustomUnitVariantSettingsResponse.getDecimalPlaces(), is(equalTo(2.0)));
        assertThat(getCustomUnitVariantSettingsResponse.isSystem(), is(false));
        assertThat(getCustomUnitVariantSettingsResponse.isCustom(), is(true));
    }
}
