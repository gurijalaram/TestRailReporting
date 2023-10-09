package com.apriori;

import com.apriori.edc.utils.ReportsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ReportsTest extends ReportsUtil {
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @TestRail(id = 9423)
    @Description("Create an EDC Report")
    public void testCreateEDCReport() {
        createEDCReport("PM_EDC", "kpatel@apriori.com", 2);
    }
}
