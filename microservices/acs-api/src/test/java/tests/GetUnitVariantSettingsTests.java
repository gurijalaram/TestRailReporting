package tests;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.UnitVariantSettingsInfoInputs;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;

import com.apriori.utils.reader.file.user.UserUtil;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;

public class GetUnitVariantSettingsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8772")
    @Description("Test Get Unit Variant Settings")
    public void testGetUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        GetUnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        ArrayList<UnitVariantSetting> allItems =
                getUnitVariantSettingsResponse.getAllUnitVariantSetting();

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
        UnitVariantSetting getCustomUnitVariantSettingsResponse =
                acsResources.getCustomUnitVariantSettings();

        assertThat(getCustomUnitVariantSettingsResponse.getType(), is(equalTo("simple")));
        assertThat(getCustomUnitVariantSettingsResponse.getName(), is(equalTo("CUSTOM")));

        String expectedMetric = getCustomUnitVariantSettingsResponse.getMetric().equals("true") ? "true" : "false";
        String expectedLength = getCustomUnitVariantSettingsResponse.getMetric().equals("true") ? "mm" : "in";
        String expectedMass = getCustomUnitVariantSettingsResponse.getMetric().equals("true") ? "kg" : "lb";

        assertThat(getCustomUnitVariantSettingsResponse.getMetric(), is(equalTo(expectedMetric)));
        assertThat(getCustomUnitVariantSettingsResponse.getLength(), is(equalTo(expectedLength)));

        if (expectedMass.equals("lb")) {
            assertThat(getCustomUnitVariantSettingsResponse.getMass(),
                    anyOf(equalTo(expectedMass), equalTo("oz")));
        } else {
            assertThat(getCustomUnitVariantSettingsResponse.getMass(), is(equalTo(expectedMass)));
        }

        assertThat(getCustomUnitVariantSettingsResponse.getTime(), is(equalTo("s")));
        assertThat(getCustomUnitVariantSettingsResponse.getDecimalPlaces(), is(equalTo(2.0)));
        assertThat(getCustomUnitVariantSettingsResponse.isSystem(), is(false));
        assertThat(getCustomUnitVariantSettingsResponse.isCustom(), is(true));
    }
}
