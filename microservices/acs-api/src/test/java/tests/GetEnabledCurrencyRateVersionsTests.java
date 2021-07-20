package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getenabledcurrencyrateversions.GetEnabledCurrencyRateVersionsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetEnabledCurrencyRateVersionsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = "")
    @Description("")*/
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources();
        GetEnabledCurrencyRateVersionsResponse getEnabledCurrencyRateVersionsResponse = acsResources
                .getEnabledCurrencyRateVersions();

        assertThat(getEnabledCurrencyRateVersionsResponse, is(notNullValue()));
    }
}
