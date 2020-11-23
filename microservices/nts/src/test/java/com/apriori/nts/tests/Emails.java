package com.apriori.nts.tests;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.nts.apicalls.NotificationService;
import com.apriori.apibase.services.nts.objects.GetEmailResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;

public class Emails {
    private PropertyStore propertyStore = new PropertyStore();

    @Test
    @TestRail(testCaseId = "3828")
    @Description("Send an email using the NTS API")
    public void sendEmail() {
        String subject = String.format("%s_%d", CommonConstants.getNtsEmailSubject(), System.currentTimeMillis());
        NotificationService.sendEmail(subject);
        Boolean emailExists = NotificationService.validateEmail(subject);
        Assert.assertEquals(true, emailExists);

    }


    @Test
    @TestRail(testCaseId = "3880")
    @Description("Get a list of emails using the NTS API")
    public void getEmails() {
        GetEmailResponse getEmailResponse = NotificationService.getEmails();
        propertyStore.setEmailIdentity(getEmailResponse.getResponse().getItems().get(0).getIdentity());
        JsonManager.serializeJsonToFile(Thread.currentThread().getContextClassLoader().getResource("property-store" +
                        ".json").getPath(),
                propertyStore);

    }


    @Test
    @TestRail(testCaseId = "3881")
    @Description("Get a single email using the NTS API")
    public void getEmail() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
                Thread.currentThread().getContextClassLoader().getResource("property-store.json").getPath(),
                PropertyStore.class);
        String identity = propertyStore.getEmailIdentity();
        NotificationService.getEmail(identity);

    }
}
