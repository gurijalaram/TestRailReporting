package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.unitvariantsettings.UnitVariantSetting;
import com.apriori.acs.api.models.response.acs.unitvariantsettings.UnitVariantSettingsResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class UnitVariantSettingsTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 8772)
    @Description("Test Get Unit Variant Settings")
    public void testGetUnitVariantSettings() {
        UnitVariantSettingsResponse getUnitVariantSettingsResponse = acsResources.getUnitVariantSettings();

        ArrayList<UnitVariantSetting> allItems = getUnitVariantSettingsResponse.getAllUnitVariantSetting();

        for (UnitVariantSetting item : allItems) {
            softAssertions.assertThat(item.getType()).isEqualTo("simple");
            softAssertions.assertThat(item.getDecimalPlaces()).isEqualTo(2.0);
            softAssertions.assertThat(item.isSystem()).isEqualTo(true);
            softAssertions.assertThat(item.isCustom()).isEqualTo(false);
            softAssertions.assertAll();
        }
    }

    @Test
    @TestRail(id = 8773)
    @Description("Test Get Custom Unit Variant Settings")
    public void testGetCustomUnitVariantSettings() {
        UnitVariantSetting getCustomUnitVariantSettingsResponse = acsResources.getCustomUnitVariantSettings();

        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getType()).isEqualTo("simple");
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getName()).isEqualTo("CUSTOM");

        List<String> potentialMassValues = new ArrayList<>(Arrays.asList("kg", "oz", "lb", "g"));

        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getMetric()).containsAnyOf("true", "false");
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getLength()).containsAnyOf("m", "mm", "ft");
        softAssertions.assertThat(potentialMassValues.contains(getCustomUnitVariantSettingsResponse.getMass())).isEqualTo(true);

        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getTime()).containsAnyOf("min", "s");
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.getDecimalPlaces()).isEqualTo(2.0);
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.isSystem()).isEqualTo(false);
        softAssertions.assertThat(getCustomUnitVariantSettingsResponse.isCustom()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
