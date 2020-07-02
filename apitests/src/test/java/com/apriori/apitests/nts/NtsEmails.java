package com.apriori.apitests.nts;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.nts.apicalls.NotificationService;
import com.apriori.apibase.services.nts.objects.GetEmailResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NtsEmails {
    private static PropertyStore propertyStore;

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(Constants.getApitestsResourcePath() +
                "/property-store.json", PropertyStore.class);
    }

    @Test
    @TestRail(testCaseId = "3880")
    @Description("Get a list of emails using the NTS API")
    public void sendEmail() {
        String subject = String.format(Constants.getNtsEmailSubject(), System.currentTimeMillis());
        NotificationService.sendEmail(subject);
        Boolean emailExists = NotificationService.validateEmail(subject);
        Assert.assertEquals(true, emailExists);

    }

    @Test
    @TestRail(testCaseId = "3880")
    @Description("Get a list of emails using the NTS API")
    public void getEmails() {
        GetEmailResponse getEmailResponse = NotificationService.getEmails();
        PropertyStore store = new PropertyStore();
        store.setEmailIdentity(getEmailResponse.getResponse().getItems().get(0).getIdentity());
        JsonManager.serializeJsonToFile(Constants.getApitestsResourcePath() +
                "/property-store.json", store);

    }


    @Test
    @TestRail(testCaseId = "3881")
    @Description("Get a single email using the NTS API")
    public void getEmail() {
        String identity = propertyStore.getEmailIdentity();
        NotificationService.getEmail(identity);

    }
}
