package tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionItemOne;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionItemTwo;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;

import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetEnabledCurrencyRateVersionsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8768")
    @Description("Test Get Enabled Currency Rate Versions")
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources();
        CurrencyRateVersionResponse getEnabledCurrencyRateVersionsResponse = acsResources
                .getEnabledCurrencyRateVersions();

        assertThat(getEnabledCurrencyRateVersionsResponse, is(notNullValue()));

        CurrencyRateVersionItemOne itemOne = getEnabledCurrencyRateVersionsResponse
                .getAbaairaairbaizqbirjqizraizraiyqbabjrizyrirjqjzqiyrbbizyq();
        CurrencyRateVersionItemTwo itemTwo = getEnabledCurrencyRateVersionsResponse
                .getAbaairabiriririqiraajraiyrabiriqirbaizqbirjriyzrajzrizyq();

        assertThat(itemOne.getBRL(), is(notNullValue()));
        assertThat(itemOne.getCAD(), is(notNullValue()));
        assertThat(itemOne.getCNY(), is(notNullValue()));
        assertThat(itemOne.getEUR(), is(notNullValue()));
        assertThat(itemOne.getUSD(), is(notNullValue()));

        assertThat(itemTwo.getBRL(), is(notNullValue()));
        assertThat(itemTwo.getCAD(), is(notNullValue()));
        assertThat(itemTwo.getCNY(), is(notNullValue()));
        assertThat(itemTwo.getEUR(), is(notNullValue()));
        assertThat(itemTwo.getUSD(), is(notNullValue()));
    }
}
