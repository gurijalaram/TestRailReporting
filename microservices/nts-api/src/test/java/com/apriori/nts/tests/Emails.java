package com.apriori.nts.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.nts.apicalls.EmailService;
import com.apriori.nts.entity.response.Email;
import com.apriori.nts.entity.response.EmailsItems;
import com.apriori.nts.entity.response.SendEmail;
import com.apriori.nts.utils.Constants;
import com.apriori.utils.TestHelper;
import com.apriori.utils.TestRail;

import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class Emails extends TestHelper {

    @Test
    @TestRail(testCaseId = {"3828", "10472", "3881"})
    @Description("Send an email using the NTS API")
    public void sendEmail() {
        String subject = Constants.EMAIL_SUBJECT + System.currentTimeMillis();
        SoftAssertions soft = new SoftAssertions();

        //Send Email
        SendEmail sendEmail = EmailService.sendEmail(
                subject,
                Constants.EMAIL_CONTENT
        ).getResponseEntity();

        //Confirm Email Sent
        Boolean emailExists = EmailService.validateEmail(subject);
        soft.assertThat(emailExists).isTrue();

        //Get Single Email
        ResponseWrapper<Email> getEmailResponse = EmailService.getEmail(sendEmail.getIdentity());
        soft.assertThat(getEmailResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        //Get All Emails, but filter by single identity
        ResponseWrapper<EmailsItems> getEmailsItemsResponse = EmailService.getEmailsByIdentity(sendEmail.getIdentity());
        soft.assertThat(getEmailsItemsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10469", "10470", "10471"})
    @Description("Send an email using the NTS API w/Attachment")
    public void sendEmailWithAttachment() {
        SoftAssertions soft = new SoftAssertions();
        String subject = Constants.EMAIL_SUBJECT + System.currentTimeMillis();

        //Send Email
        SendEmail sendEmail = EmailService.sendEmailWithAttachment(
                subject,
                "NtsAttachment.txt",
                Constants.EMAIL_CONTENT_W_ATTACHMENT
        ).getResponseEntity();

        //Confirm Email Sent
        Boolean emailExists = EmailService.validateEmail(subject);
        soft.assertThat(emailExists).isTrue();

        //Get Single Email
        ResponseWrapper<Email> getEmailResponse = EmailService.getEmail(sendEmail.getIdentity());
        soft.assertThat(getEmailResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        //Get All Emails, but filter by single identity
        ResponseWrapper<EmailsItems> getEmailsItemsResponse = EmailService.getEmailsByIdentity(sendEmail.getIdentity());
        soft.assertThat(getEmailsItemsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3880"})
    @Description("Get a list of emails using the NTS API")
    public void getEmails() {
        ResponseWrapper<EmailsItems> getEmailResponse = EmailService.getEmails();
        assertEquals(getEmailResponse.getStatusCode(), HttpStatus.SC_OK);
    }
}
