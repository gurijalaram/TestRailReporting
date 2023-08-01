package com.apriori;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.edcapi.utils.ReportsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReportsTest extends ReportsUtil {
    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = 9423)
    @Description("Create an EDC Report")
    public void testCreateEDCReport() {
        createEDCReport("PM_EDC", "kpatel@apriori.com", 2);
    }
}
