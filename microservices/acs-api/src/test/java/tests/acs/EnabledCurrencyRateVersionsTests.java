package tests.acs;

import com.apriori.TestUtil;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class EnabledCurrencyRateVersionsTests extends TestUtil {

    @Test
    @TestRail(id = 8768)
    @Description("Test Get Enabled Currency Rate Versions")
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources();
        acsResources.getEnabledCurrencyRateVersions();
    }
}
