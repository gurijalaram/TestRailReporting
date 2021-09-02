package com.apriori.nts.tests;

import com.apriori.nts.utils.NotificationService;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.junit.Test;

public class Notifications {

    @Test
    @TestRail(testCaseId = {"4530"})
    @Description("Get a list of notifications using the NTS API")
    public void getNotifications() {
        NotificationService.getNotifications(PropertiesContext.get("${env}.nts.api_url"), PropertiesContext.get("${env}.secret_key"));
    }
}
