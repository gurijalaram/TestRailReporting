package tests;

import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;

import com.apriori.utils.TestRail;

import com.apriori.utils.properties.PropertiesContext;
import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetEnabledCurrencyRateVersionsTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        PropertiesContext.getStr("${env}.base_url");
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8768")
    @Description("Test Get Enabled Currency Rate Versions")
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources();
        acsResources.getEnabledCurrencyRateVersions();
    }
}
