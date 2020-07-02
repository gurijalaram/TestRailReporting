package com.apriori.apitests.nts;

import com.apriori.apibase.services.nts.apicalls.NotificationService;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class NtsNotifications {
    @Test
    @TestRail(testCaseId = "4530")
    @Description("Get a list of notifications using the NTS API")
    public void getNotifications() {
        NotificationService.getNotifications();

    }
}
