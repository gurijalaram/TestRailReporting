package com.apriori.edcapi.tests;

import com.apriori.edcapi.utils.ReportsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class ReportsTest extends ReportsUtil {
    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = "9423")
    @Description("Create an EDC Report")
    public void testCreateEDCReport() {
        createEDCReport("PM_EDC", "kpatel@apriori.com", 2);
    }
}
