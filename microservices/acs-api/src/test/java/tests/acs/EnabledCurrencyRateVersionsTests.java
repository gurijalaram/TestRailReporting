package tests.acs;

import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class EnabledCurrencyRateVersionsTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "8768")
    @Description("Test Get Enabled Currency Rate Versions")
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources();
        acsResources.getEnabledCurrencyRateVersions();
    }
}
