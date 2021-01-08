package com.apriori.apibase.services.nts.apicalls;

import com.apriori.apibase.services.nts.objects.Email;
import com.apriori.apibase.services.nts.objects.GetEmailResponse;
import com.apriori.apibase.services.nts.objects.Notifications;
import com.apriori.apibase.services.nts.objects.SendEmailResponse;
import com.apriori.apibase.services.nts.utils.EmailSetup;
import com.apriori.utils.EmailUtil;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.ConnectionManager;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;

public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static String baseUrl =
            "https://" + CommonConstants.getNtsServiceHost() + "/emails%s?key=" + CommonConstants.getSecretKey();

    public static Boolean validateEmail(String subject) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        try {
            int count = 0;
            int defaultTimeout = 12;
            while (count <= defaultTimeout) {
                Message[] messages = EmailUtil.getEmail(
                        emailSetup.getHost(),
                        emailSetup.getUsername(),
                        emailSetup.getPassword()
                );
                Message message = messages[messages.length - 1];

                if (message.getSubject().toLowerCase().equals(subject.toLowerCase())) {
                    return true;
                } else {
                    Thread.sleep(5000);
                }
                count += 1;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    private static SendEmailResponse sendEmail(String subject, Map<String, String> parameters) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        Map<String, String> params = new HashMap<String, String>();
        params.put("recipientAddress", emailSetup.getUsername());
        params.put("subject", subject);
        params.put("content", CommonConstants.getNtsEmailContent());

        if (parameters != null) {
            params.putAll(parameters);
        }

        String url = String.format(baseUrl, "");

        SendEmailResponse smr = null;
        try {
            smr = (SendEmailResponse)ConnectionManager.postMultPartFormData(url, params, SendEmailResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return smr;

    }

    public static SendEmailResponse sendEmail(String subject) {
        return sendEmail(subject, null);
    }

    public static SendEmailResponse sendEmailWithTemplate(String subject, String templateName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("templateName", templateName);
        return sendEmail(subject, params);
    }

    public static SendEmailResponse sendEmailAsBatch(String subject, String batchIdentifier) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sendAsBatch", "true");
        params.put("batchIdentifier", batchIdentifier);

        return sendEmail(subject, params);
    }

    public static SendEmailResponse sendEmailWithAttachment(String subject, String attachmentFile) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        Map<String, String> params = new HashMap<String, String>();
        params.put("recipientAddress", emailSetup.getUsername());
        params.put("subject", subject);
        params.put("content", CommonConstants.getNtsEmailContent());

        String url = String.format(baseUrl, "");

        SendEmailResponse smr = null;
        File attachment = new File(attachmentFile);
        try {
            smr = (SendEmailResponse)ConnectionManager.postMultPartFormData(url, params, SendEmailResponse.class, attachment);
        } catch (Exception e) {
            logger.error(e.getMessage());;
        }

        return smr;

    }

    public static GetEmailResponse getEmails() {

        String url = String.format(baseUrl, "");
        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", CommonConstants.getNtsTargetCloudContext());

        return (GetEmailResponse) GenericRequestUtil.get(
                RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static Email getEmail(String identity) {
        String url = String.format(baseUrl, "/" + identity);

        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", CommonConstants.getNtsTargetCloudContext());

        return (Email) GenericRequestUtil.get(
                RequestEntity.init(url, Email.class).setHeaders(headers),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static Notifications getNotifications() {
        String url = "https://" + CommonConstants.getNtsServiceHost() + "/notifications?key=" + CommonConstants.getSecretKey();
        return (Notifications) GenericRequestUtil.get(
                RequestEntity.init(url, Notifications.class),
                new RequestAreaApi()
        ).getResponseEntity();
    }

}