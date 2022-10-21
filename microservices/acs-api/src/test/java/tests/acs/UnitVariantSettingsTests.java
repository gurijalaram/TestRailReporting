package tests.acs;

import com.apriori.acs.entity.response.acs.unitvariantsettings.UnitVariantSetting;
import com.apriori.acs.entity.response.acs.unitvariantsettings.UnitVariantSettingsResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitVariantSettingsTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "8772")
    @Description("Test Get Unit Variant Settings")
    public void testGetUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        UnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        ArrayList<UnitVariantSetting> allItems = getUnitVariantSettingsResponse.getAllUnitVariantSetting();

        for (UnitVariantSetting item : allItems) {
            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(item.getType()).isEqualTo("simple");
            softAssertions.assertThat(item.getDecimalPlaces()).isEqualTo(2.0);
            softAssertions.assertThat(item.isSystem()).isEqualTo(true);
            softAssertions.assertThat(item.isCustom()).isEqualTo(false);
            softAssertions.assertAll();
        }
    }

    @Test
    @TestRail(testCaseId = "8773")
    @Description("Test Get Custom Unit Variant Settings")
    public void testGetCustomUnitVariantSettings() {
        AcsResources acsResources = new AcsResources();
        UnitVariantSetting getCustomUnitVariantSettingsResponse = acsResources.getCustomUnitVariantSettings();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getType()).isEqualTo("simple");
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getName()).isEqualTo("CUSTOM");

        String expectedMetric = getCustomUnitVariantSettingsResponse.getMetric().equals("true") ? "true" : "false";
        String expectedLength = getCustomUnitVariantSettingsResponse.getLength().equals("true") ? "mm" : "ft";
        List<String> potentialMassValues = new ArrayList<>(Arrays.asList("kg", "oz", "lb"));

        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getMetric()).isEqualTo(expectedMetric);
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getLength()).isEqualTo(expectedLength);
        softAssertions.assertThat(potentialMassValues.contains(getCustomUnitVariantSettingsResponse.getMass())).isEqualTo(true);

        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getTime()).isEqualTo("min");
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getDecimalPlaces()).isEqualTo(2.0);
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.isSystem()).isEqualTo(false);
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.isCustom()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
