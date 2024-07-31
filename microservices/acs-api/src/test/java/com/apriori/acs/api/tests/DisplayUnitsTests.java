package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.displayunits.DisplayUnitsInputs;
import com.apriori.acs.api.models.response.acs.displayunits.DisplayUnitsResponse;
import com.apriori.acs.api.models.response.acs.displayunits.UnitVariantSettingsInfoInputs;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.CurrencyEnum;
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

@ExtendWith(TestRulesAPI.class)
public class DisplayUnitsTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 8769)
    @Description("Test Get Display Units")
    public void testGetDisplayUnits() {
        DisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        softAssertions.assertThat(getDisplayUnitsResponse).isNotNull();
        softAssertions.assertThat(getDisplayUnitsResponse.getCurrencyLabel())
            .isEqualTo("abaairaairbaizqbirjqizraizraiyqbabjrizyrirjqjzqiyrbbizyq");

        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType())
            .isEqualTo("simple");
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName()).containsAnyOf("MMKS", "CUSTOM");

        if (getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric().equals("false")) {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "in", "lb");
        } else {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "mm", "kg");
        }

        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces()).isEqualTo(2);
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem()).isNotNull();
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 8770)
    @Description("Test Set Currency Display Unit")
    public void setCurrencyDisplayUnitTest() {
        DisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String currencyCodeToCheck = getDisplayUnitsResponse.getCurrencyCode().equals(CurrencyEnum.USD.getCurrency()) ? CurrencyEnum.GBP.getCurrency() : CurrencyEnum.USD.getCurrency();

        GenericResourceCreatedResponse setDisplayUnitsResponse = acsResources
                .setDisplayUnits(DisplayUnitsInputs.builder()
                        .currencyCode(currencyCodeToCheck)
                        .currencyLabel(getDisplayUnitsResponse.getCurrencyLabel())
                        .unitVariantSettingsInfo(UnitVariantSettingsInfoInputs.builder()
                                .type(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType())
                                .name(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName())
                                .metric(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric())
                                .length(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength())
                                .mass(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass())
                                .time(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime())
                                .decimalPlaces(getDisplayUnitsResponse.getUnitVariantSettingsInfo()
                                        .getDecimalPlaces())
                                .system(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem())
                                .custom(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom())
                            .build())
                .build());

        softAssertions.assertThat(setDisplayUnitsResponse.getResourceCreated()).isEqualTo("false");

        DisplayUnitsResponse getDisplayUnitsResponsePostChanges = acsResources.getDisplayUnits();
        softAssertions.assertThat(getDisplayUnitsResponsePostChanges.getCurrencyCode()).isEqualTo(currencyCodeToCheck);
        softAssertions.assertAll();

        acsResources.resetDisplayUnits();
    }

    @Test
    @TestRail(id = 8771)
    @Description("Test Set Length and Mass Display Unit")
    public void setLengthAndMassDisplayUnitTest() {
        DisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String lengthToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength().equals("mm") ? "in" : "mm";
        String massToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass().equals("kg") ? "lb" : "kg";
        String metricToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric().equals("true") ? "false" : "true";

        GenericResourceCreatedResponse setDisplayUnitsResponse = acsResources
                .setDisplayUnits(DisplayUnitsInputs.builder()
                        .currencyCode(getDisplayUnitsResponse.getCurrencyCode())
                        .currencyLabel(getDisplayUnitsResponse.getCurrencyLabel())
                        .unitVariantSettingsInfo(UnitVariantSettingsInfoInputs.builder()
                                .type(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType())
                                .name("CUSTOM")
                                .metric(metricToSet)
                                .length(lengthToSet)
                                .mass(massToSet)
                                .time(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime())
                                .decimalPlaces(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces())
                                .system(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem())
                                .custom(true)
                            .build())
                .build());

        softAssertions.assertThat(setDisplayUnitsResponse.getResourceCreated()).isEqualTo("false");

        DisplayUnitsResponse getDisplayUnitResponsePostChanges = acsResources.getDisplayUnits();
        String isMetric = lengthToSet.equals("mm") ? "true" : "false";

        softAssertions.assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMetric()).isEqualTo(isMetric);
        softAssertions.assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getLength()).isEqualTo(lengthToSet);
        softAssertions.assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMass()).isEqualTo(massToSet);
        softAssertions.assertAll();
    }

    private void getDisplayUnitsAssertions(DisplayUnitsResponse getDisplayUnitsResponse, String length, String mass) {
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength()).isEqualTo(length);
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass()).isEqualTo(mass);
        softAssertions.assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime()).isEqualTo("s");
        softAssertions.assertAll();
    }
}
