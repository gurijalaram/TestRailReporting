package com.apriori;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import com.apriori.http.utils.ResponseWrapper;
import com.apriori.nts.email.EmailService;
import com.apriori.nts.models.response.Email;
import com.apriori.nts.models.response.EmailsItems;
import com.apriori.nts.models.response.SendEmail;
import com.apriori.nts.utils.Constants;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

@ExtendWith(TestRulesApi.class)
public class EmailsTests {

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {3828, 10472, 3881})
    @Description("Send an email using the NTS API")
    public void sendEmail() {
        EmailService emailService = new EmailService();
        String subject = "\"" + Constants.EMAIL_SUBJECT + System.currentTimeMillis() + "\"";

        SoftAssertions soft = new SoftAssertions();

        //Send Email
        SendEmail sendEmail = emailService.sendEmail(
            subject,
            Constants.EMAIL_CONTENT).getResponseEntity();

        //Confirm Email Sent
        Boolean emailExists = emailService.validateEmail(subject);
        soft.assertThat(emailExists).isTrue();

        //Get Single Email
        ResponseWrapper<Email> getEmailResponse = emailService.getEmail(sendEmail.getIdentity());
        soft.assertThat(getEmailResponse.getResponseEntity().getSubject()).isEqualTo(subject);

        //Get All Emails, but filter by single identity
        ResponseWrapper<EmailsItems> getEmailsItemsResponse = emailService.getEmailsByIdentity(sendEmail.getIdentity());
        soft.assertThat(getEmailsItemsResponse.getResponseEntity().getItems()).hasSizeGreaterThan(0);

        soft.assertAll();
    }

    @Test
    @TestRail(id = {10469, 10470, 10471})
    @Description("Send an email using the NTS API w/Attachment")
    public void sendEmailWithAttachment() {
        EmailService emailService = new EmailService();

        SoftAssertions soft = new SoftAssertions();
        String subject = "\"" + Constants.EMAIL_SUBJECT + System.currentTimeMillis() + "\"";

        //Send Email
        SendEmail sendEmail = emailService.sendEmailWithAttachment(
            subject,
            "NtsAttachment.txt",
            Constants.EMAIL_CONTENT_W_ATTACHMENT).getResponseEntity();

        //Confirm Email Sent
        Boolean emailExists = emailService.validateEmail(subject);
        soft.assertThat(emailExists).isTrue();

        //Get Single Email
        ResponseWrapper<Email> getEmailResponse = emailService.getEmail(sendEmail.getIdentity());
        soft.assertThat(getEmailResponse.getResponseEntity().getSubject()).isEqualTo(subject);

        //Get All Emails, but filter by single identity
        ResponseWrapper<EmailsItems> getEmailsItemsResponse = emailService.getEmailsByIdentity(sendEmail.getIdentity());
        soft.assertThat(getEmailsItemsResponse.getResponseEntity().getItems()).hasSizeGreaterThan(0);

        soft.assertAll();
    }

    @Test
    @TestRail(id = {3880})
    @Description("Get a list of emails using the NTS API")
    public void getEmails() {
        EmailService emailService = new EmailService();

        assertThat(emailService.getEmails().getResponseEntity().getItems(), hasSize(greaterThanOrEqualTo(0)));
    }
}
