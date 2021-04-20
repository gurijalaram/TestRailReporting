package com.apriori.nts.tests;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.nts.apicalls.NotificationService;
import com.apriori.apibase.services.nts.objects.GetEmailResponse;
import com.apriori.nts.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Emails {
    private PropertyStore propertyStore = new PropertyStore();
    private String baseUrl;

    @Before
    public void setServiceUrl() {
        baseUrl = "https://" + Constants.getNtsServiceHost() + "/emails%s?key=" + Constants.getSecretKey();
    }

    @Test
    @TestRail(testCaseId = {"3828"})
    @Description("Send an email using the NTS API")
    public void sendEmail() {
        String subject = String.format("%s_%d", Constants.getNtsEmailSubject(), System.currentTimeMillis());
        NotificationService.sendEmail(baseUrl, subject, Constants.getNtsEmailContent(), Constants.getTargetCloudContext());
        Boolean emailExists = NotificationService.validateEmail(subject);
        Assert.assertEquals(true, emailExists);
    }


    @Test
    @TestRail(testCaseId = {"3880"})
    @Description("Get a list of emails using the NTS API")
    public void getEmails() {
        GetEmailResponse getEmailResponse = NotificationService.getEmails(baseUrl, Constants.getTargetCloudContext());
        propertyStore.setEmailIdentity(getEmailResponse.getResponse().getItems().get(0).getIdentity());
        JsonManager.serializeJsonToFile(FileResourceUtil.getResourceAsFile("property-store.json").getPath(),
            propertyStore);
    }

    @Test
    @TestRail(testCaseId = {"3881"})
    @Description("Get a single email using the NTS API")
    public void getEmail() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("property-store.json"),
            PropertyStore.class);
        String identity = propertyStore.getEmailIdentity();
        NotificationService.getEmail(baseUrl, identity, Constants.getTargetCloudContext());
    }
}
