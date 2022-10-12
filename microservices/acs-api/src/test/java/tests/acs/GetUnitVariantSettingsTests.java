package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.acs.getunitvariantsettings.UnitVariantSetting;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUnitVariantSettingsTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "8772")
    @Description("Test Get Unit Variant Settings")
    public void testGetUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        GetUnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        ArrayList<UnitVariantSetting> allItems = getUnitVariantSettingsResponse.getAllUnitVariantSetting();

        for (UnitVariantSetting item : allItems) {
            assertThat(item.getType(), is(equalTo("simple")));
            assertThat(item.getDecimalPlaces(), is(equalTo(2.0)));
            assertThat(item.isSystem(), is(equalTo(true)));
            assertThat(item.isCustom(), is(equalTo(false)));
        }
    }

    @Test
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
        List<String> potentialMassValues = new ArrayList<>(Arrays.asList("kg", "oz", "lb"));

        assertThat(getCustomUnitVariantSettingsResponse.getMetric(), is(equalTo(expectedMetric)));
        assertThat(getCustomUnitVariantSettingsResponse.getLength(), is(equalTo(expectedLength)));
        assertThat(potentialMassValues.contains(getCustomUnitVariantSettingsResponse.getMass()), is(equalTo(true)));

        assertThat(getCustomUnitVariantSettingsResponse.getTime(), is(equalTo("s")));
        assertThat(getCustomUnitVariantSettingsResponse.getDecimalPlaces(), is(equalTo(2.0)));
        assertThat(getCustomUnitVariantSettingsResponse.isSystem(), is(false));
        assertThat(getCustomUnitVariantSettingsResponse.isCustom(), is(true));
    }
}
