package com.apriori.nts.apicalls;

import com.apriori.nts.entity.response.GetEmailResponse;
import com.apriori.nts.entity.response.Notifications;
import com.apriori.nts.entity.response.SendEmailResponse;
import com.apriori.nts.utils.EmailSetup;
import com.apriori.utils.EmailUtil;
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

    /**
     * Validates that an email has been sent by checking the target account
     *
     * @param subject Email subject
     * @return True, if the email exits
     */
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

    /**
     * Call the SendEmail endpoint
     *
     * @param baseUrl Base url of the NTS Service
     * @param subject Email subject
     * @param parameters
     * @param emailContent Email content
     * @param cloudContext Cloud context key
     * @return Service response
     */
    private static SendEmailResponse sendEmail(String baseUrl, String subject, Map<String, String> parameters, String emailContent, String cloudContext) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        Map<String, String> params = new HashMap<>();
        params.put("recipientAddress", emailSetup.getUsername());
        params.put("subject", subject);
        params.put("content", emailContent);

        if (parameters != null) {
            params.putAll(parameters);
        }

        String url = String.format(baseUrl, "");

        SendEmailResponse smr = null;
        try {
            smr = (SendEmailResponse) ConnectionManager.postMultiPartFormData(url, params, SendEmailResponse.class, cloudContext);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return smr;

    }

    /**
     * Call the SendEmail service endpoint
     *
     * @param baseUrl Base url of the NTS Service
     * @param subject Email subject
     * @param emailContent Email content
     * @param cloudContext Cloud context key
     * @return Service response
     */
    public static SendEmailResponse sendEmail(String baseUrl, String subject, String emailContent, String cloudContext) {
        return sendEmail(baseUrl, subject, null, emailContent, cloudContext);
    }

    /**
     * Call the SendEmail service endpoint with a template
     *
     * @param baseUrl Base url of the NTS Service
     * @param subject Email subject
     * @param templateName The template to use
     * @param emailContent Email content
     * @param cloudContext Cloud context key
     * @return Service response
     */
    public static SendEmailResponse sendEmailWithTemplate(String baseUrl, String subject, String templateName, String emailContent, String cloudContext) {
        Map<String, String> params = new HashMap<>();
        params.put("templateName", templateName);
        return sendEmail(baseUrl, subject, params, emailContent, cloudContext);
    }

    /**
     * Call the SendEmail service endpoint as a batch
     *
     * @param baseUrl Base url of the NTS Service
     * @param subject Email subject
     * @param batchIdentifier The batch id
     * @param emailContent Email content
     * @param cloudContext Cloud context key
     * @return Service response
     */
    public static SendEmailResponse sendEmailAsBatch(String baseUrl, String subject, String batchIdentifier, String emailContent, String cloudContext) {
        Map<String, String> params = new HashMap<>();
        params.put("sendAsBatch", "true");
        params.put("batchIdentifier", batchIdentifier);

        return sendEmail(baseUrl, subject, params, emailContent, cloudContext);
    }

    /**
     *
     * @param baseUrl
     * @param subject
     * @param attachmentFile
     * @param emailContent
     * @param cloudContext
     * @return
     */
    public static SendEmailResponse sendEmailWithAttachment(String baseUrl, String subject, String attachmentFile, String emailContent, String cloudContext) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        Map<String, String> params = new HashMap<>();
        params.put("recipientAddress", emailSetup.getUsername());
        params.put("subject", subject);
        params.put("content", emailContent);

        String url = String.format(baseUrl, "");

        SendEmailResponse smr = null;
        File attachment = new File(attachmentFile);
        try {
            smr = (SendEmailResponse) ConnectionManager.postMultiPartFormData(url, params, SendEmailResponse.class, attachment, cloudContext);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return smr;
    }

    /**
     * Call the GetEmail service endpoint
     *
     * @param baseUrl Base url of the NTS Service
     * @param cloudContext Cloud context key
     * @return Service response
     */
    public static GetEmailResponse getEmails(String baseUrl, String cloudContext) {

        String url = String.format(baseUrl, "");
        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", cloudContext);

        return (GetEmailResponse) GenericRequestUtil.get(
            RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
            new RequestAreaApi()
        ).getResponseEntity();
    }

    /**
     * Call the GetEmails service endpoint
     *
     * @param baseUrl Base url of the NTS Service
     * @param identity The identity of the email to retrieve
     * @param cloudContext Cloud context key
     * @return Service response
     */
    public static GetEmailResponse getEmail(String baseUrl, String identity, String cloudContext) {
        String url = String.format(baseUrl, "/" + identity);

        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", cloudContext);

        return (GetEmailResponse) GenericRequestUtil.get(
            RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
            new RequestAreaApi()
        ).getResponseEntity();
    }

    /**
     * Call the GetNotifications endpoint
     * 
     * @param serviceHost NTS host
     * @param secretKey The environments secret key
     * @return Notifications response
     */
    public static Notifications getNotifications(String serviceHost, String secretKey) {
        String url = "https://" + serviceHost + "/notifications?key=" + secretKey;
        return (Notifications) GenericRequestUtil.get(
            RequestEntity.init(url, Notifications.class),
            new RequestAreaApi()
        ).getResponseEntity();
    }
}