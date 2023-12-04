package com.apriori.edc.api.tests;

import com.apriori.edc.api.utils.ReportsUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ReportsTest extends ReportsUtil {
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @TestRail(id = 9423)
    @Description("Create an EDC Report")
    public void testCreateEDCReport() {
        createEDCReport("PM_EDC", "kpatel@apriori.com", 2);
    }
}
