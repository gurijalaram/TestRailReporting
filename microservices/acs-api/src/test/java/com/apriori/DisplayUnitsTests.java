package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;

import com.apriori.acs.models.response.acs.displayunits.DisplayUnitsInputs;
import com.apriori.acs.models.response.acs.displayunits.DisplayUnitsResponse;
import com.apriori.acs.models.response.acs.displayunits.UnitVariantSettingsInfoInputs;
import com.apriori.acs.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.enums.CurrencyEnum;
import com.apriori.http.utils.TestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class DisplayUnitsTests extends TestUtil {

    @Test
    @TestRail(id = 8769)
    @Description("Test Get Display Units")
    public void testGetDisplayUnits() {
        AcsResources acsResources = new AcsResources();
        DisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        assertThat(getDisplayUnitsResponse, is(notNullValue()));

        assertThat(getDisplayUnitsResponse.getCurrencyLabel(), is(equalTo("abaairaairbaizqbirjqizraizraiyqbabjrizyrirjqjzqiyrbbizyq")));

        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType(), is(equalTo("simple")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName(), anyOf(equalTo("MMKS"), equalTo("CUSTOM")));

        if (getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric().equals("false")) {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "in", "lb");
        } else {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "mm", "kg");
        }

        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces(), is(equalTo(2)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem(), is(notNullValue()));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom(), is(notNullValue()));
    }

    @Test
    @TestRail(id = 8770)
    @Description("Test Set Currency Display Unit")
    public void setCurrencyDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
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

        assertThat(setDisplayUnitsResponse.getResourceCreated(), is(equalTo("false")));

        DisplayUnitsResponse getDisplayUnitsResponsePostChanges = acsResources.getDisplayUnits();
        assertThat(getDisplayUnitsResponsePostChanges.getCurrencyCode(), is(equalTo(currencyCodeToCheck)));
    }

    @Test
    @TestRail(id = 8771)
    @Description("Test Set Length and Mass Display Unit")
    public void setLengthAndMassDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
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

        assertThat(setDisplayUnitsResponse.getResourceCreated(), is(equalTo("false")));

        DisplayUnitsResponse getDisplayUnitResponsePostChanges = acsResources.getDisplayUnits();
        String isMetric = lengthToSet.equals("mm") ? "true" : "false";

        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMetric(), is(equalTo(isMetric)));
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getLength(), is(equalTo(lengthToSet)));
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMass(), is(equalTo(massToSet)));
    }

    private void getDisplayUnitsAssertions(DisplayUnitsResponse getDisplayUnitsResponse, String length, String mass) {
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength(), is(equalTo(length)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass(), is(equalTo(mass)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime(), is(equalTo("s")));
    }
}
