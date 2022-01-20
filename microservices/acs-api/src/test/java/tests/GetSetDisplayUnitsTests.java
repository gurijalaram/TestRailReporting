package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.UnitVariantSettingsInfoInputs;
import com.apriori.acs.utils.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetDisplayUnitsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8769")
    @Description("Test Get Display Units")
    public void testGetDisplayUnits() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        assertThat(getDisplayUnitsResponse, is(notNullValue()));

        assertThat(getDisplayUnitsResponse.getCurrencyLabel(), is(equalTo(
                "abaairaairbaizqbirjqizraizraiyqbabjrizyrirjqjzqiyrbbizyq")));

        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType(), is(equalTo("simple")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName(), is(equalTo("MMKS")));

        if (getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric().equals("false")) {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "in", "lb");
        } else {
            getDisplayUnitsAssertions(getDisplayUnitsResponse, "mm", "kg");
        }

        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces(), is(equalTo(2)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem(), is(equalTo(true)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom(), is(equalTo(false)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8770")
    @Description("Test Set Currency Display Unit")
    public void setCurrencyDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String currencyCodeToCheck = getDisplayUnitsResponse.getCurrencyCode().equals(CurrencyEnum.USD.getCurrency())
                ? CurrencyEnum.GBP.getCurrency() : CurrencyEnum.USD.getCurrency();

        GenericResourceCreatedResponse setDisplayUnitsResponse = acsResources
                .setDisplayUnits(SetDisplayUnitsInputs.builder()
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

        GetDisplayUnitsResponse getDisplayUnitsResponsePostChanges = acsResources.getDisplayUnits();
        assertThat(getDisplayUnitsResponsePostChanges.getCurrencyCode(), is(equalTo(currencyCodeToCheck)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8771")
    @Description("Test Set Length and Mass Display Unit")
    public void setLengthAndMassDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String lengthToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength().equals("mm")
                ? "in" : "mm";
        String massToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass().equals("kg")
                ? "lb" : "kg";
        String metricToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo()
                .getMetric().equals("true") ? "false" : "true";

        GenericResourceCreatedResponse setDisplayUnitsResponse = acsResources
                .setDisplayUnits(SetDisplayUnitsInputs.builder()
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

        GetDisplayUnitsResponse getDisplayUnitResponsePostChanges = acsResources.getDisplayUnits();
        String isMetric = lengthToSet.equals("mm") ? "true" : "false";
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMetric(),
                is(equalTo(isMetric)));
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getLength(),
                is(equalTo(lengthToSet)));
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMass(),
                is(equalTo(massToSet)));
    }

    private void getDisplayUnitsAssertions(GetDisplayUnitsResponse getDisplayUnitsResponse, String length, String mass) {
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength(), is(equalTo(length)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass(), is(equalTo(mass)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime(), is(equalTo("s")));
    }
}
