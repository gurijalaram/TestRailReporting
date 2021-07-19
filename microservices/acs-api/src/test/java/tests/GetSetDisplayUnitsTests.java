package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.UnitVariantSettingsInfoInputs;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import org.checkerframework.checker.units.qual.A;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetSetDisplayUnitsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = "")
    @Description("")*/
    public void testGetDisplayUnits() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        assertThat(getDisplayUnitsResponse, is(notNullValue()));

        assertThat(getDisplayUnitsResponse.getCurrencyCode(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(getDisplayUnitsResponse.getCurrencyLabel(), is(equalTo(
                "abaairaairbaizqbirjqizraizraiyqbabjrizyrirjqjzqiyrbbizyq")));
        assertThat(getDisplayUnitsResponse.getCurrencyExchangeRate(), is(equalTo(1.0)));

        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType(), is(equalTo("simple")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName(), is(equalTo("CUSTOM")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric(), is(equalTo("true")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength(), is(equalTo("mm")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass(), is(equalTo("kg")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime(), is(equalTo("s")));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces(), is(equalTo(2)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem(), is(equalTo(false)));
        assertThat(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom(), is(equalTo(true)));
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = ""
    @Description("")
     */
    public void setCurrencyDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String currencyCodeToCheck = getDisplayUnitsResponse.getCurrencyCode().equals(CurrencyEnum.USD.getCurrency())
                ? CurrencyEnum.GBP.getCurrency() : CurrencyEnum.USD.getCurrency();

        SetDisplayUnitsResponse setDisplayUnitsResponse = acsResources
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

        assertThat(setDisplayUnitsResponse, is(notNullValue()));
        assertThat(setDisplayUnitsResponse.getResourceCreated(), is(equalTo("false")));

        GetDisplayUnitsResponse getDisplayUnitsResponsePostChanges = acsResources.getDisplayUnits();
        assertThat(getDisplayUnitsResponsePostChanges.getCurrencyCode(), is(equalTo(currencyCodeToCheck)));
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = ""
    @Description("")
     */
    public void setLengthMassTimeDisplayUnitTest() {
        AcsResources acsResources = new AcsResources();
        GetDisplayUnitsResponse getDisplayUnitsResponse = acsResources.getDisplayUnits();

        String lengthToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getLength().equals("mm") ? "m" : "mm";
        String massToSet = getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMass().equals("kg") ? "g" : "kg";

        SetDisplayUnitsResponse setDisplayUnitsResponse = acsResources
                .setDisplayUnits(SetDisplayUnitsInputs.builder()
                        .currencyCode(getDisplayUnitsResponse.getCurrencyCode())
                        .currencyLabel(getDisplayUnitsResponse.getCurrencyLabel())
                        .unitVariantSettingsInfo(UnitVariantSettingsInfoInputs.builder()
                                .type(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getType())
                                .name(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getName())
                                .metric(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getMetric())
                                .length(lengthToSet)
                                .mass(massToSet)
                                .time(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getTime())
                                .decimalPlaces(getDisplayUnitsResponse.getUnitVariantSettingsInfo().getDecimalPlaces())
                                .system(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isSystem())
                                .custom(getDisplayUnitsResponse.getUnitVariantSettingsInfo().isCustom())
                                .build())
                .build());

        assertThat(setDisplayUnitsResponse, is(notNullValue()));
        assertThat(setDisplayUnitsResponse.getResourceCreated(), is(equalTo("false")));

        GetDisplayUnitsResponse getDisplayUnitResponsePostChanges = acsResources.getDisplayUnits();
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getLength(),
                is(equalTo(lengthToSet)));
        assertThat(getDisplayUnitResponsePostChanges.getUnitVariantSettingsInfo().getMass(),
                is(equalTo(massToSet)));
    }
}
