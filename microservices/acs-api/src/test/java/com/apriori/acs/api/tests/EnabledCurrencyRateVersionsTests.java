package com.apriori.acs.api.tests;

import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class EnabledCurrencyRateVersionsTests extends TestUtil {
    private final UserCredentials userCredentials = UserUtil.getUser("common");

    @Test
    @TestRail(id = 8768)
    @Description("Test Get Enabled Currency Rate Versions")
    public void testGetEnabledCurrencyRateVersions() {
        AcsResources acsResources = new AcsResources(userCredentials);
        acsResources.getEnabledCurrencyRateVersions();
    }
}
