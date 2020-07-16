package com.apriori.apibase.services.nts.apicalls;

import com.apriori.apibase.services.nts.objects.Email;
import com.apriori.apibase.services.nts.objects.GetEmailResponse;
import com.apriori.apibase.services.nts.objects.Notifications;
import com.apriori.apibase.services.nts.objects.SendEmailResponse;
import com.apriori.apibase.services.nts.utils.EmailSetup;
import com.apriori.utils.EmailUtil;
import com.apriori.utils.EncryptionUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;

public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);
    private static String url = "https://";
    
    private static RequestEntity defaultEmailRequest(String subject) {
        url = url.concat(Constants.getNtsServiceHost() + "/emails?key=" + Constants.getSecretKey());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        headers.put("ap-cloud-context", Constants.getNtsTargetCloudContext());
        RequestEntity requestEntity = RequestEntity.init(url, SendEmailResponse.class)
                .setHeaders(headers)
                .setFormParams(new FormParams()
                        .use("recipientAddress", Constants.getNtsEmailRecipientAddress())
                        .use("subject", subject)
                        .use("content", Constants.getNtsEmailContent()));

        return requestEntity;
    }

    public static Boolean validateEmail(String subject) {
        EmailSetup.getCredentials();

        try {
            Message[] messages = EmailUtil.getEmail(
                    EmailSetup.getAddress(),
                    EmailSetup.getUsername(),
                    EmailSetup.getPassword()
            );

            for (Message message : messages) {
                if (message.getSubject().toLowerCase().equals(subject)) {
                    return true;
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage());

        }

        return false;
    }

    public static SendEmailResponse sendEmail(String subject) {
        RequestEntity requestEntity = defaultEmailRequest(subject);
        return (SendEmailResponse) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();

    }

    public static SendEmailResponse sendEmailWithTemplate(String subject, String templateName) {
        RequestEntity requestEntity = defaultEmailRequest(subject);
        requestEntity.setFormParams(requestEntity.getFormParams()
               .use("templateName", templateName));

        return (SendEmailResponse) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    public static SendEmailResponse sendEmailAsBatch(String subject, String batchIdentifier) {
        RequestEntity requestEntity = defaultEmailRequest(subject);
        requestEntity.setFormParams(requestEntity.getFormParams()
                .use("sendAsBatch", "true")
                .use("batchIdentifier", batchIdentifier)
        );

        return (SendEmailResponse) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    public static SendEmailResponse sendEmailWithAttacment(String subject) {
        RequestEntity requestEntity = defaultEmailRequest(subject);
        requestEntity
                .setMultiPartFiles(new MultiPartFiles()
                        .use("data", FileResourceUtil.getLocalResourceFile(Constants.getNtsEmailAttachment())
                )
        );

        return (SendEmailResponse) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    public static GetEmailResponse getEmails() {
        url = url.concat(Constants.getNtsServiceHost() + "/emails?key=" + Constants.getSecretKey());
        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", Constants.getNtsTargetCloudContext());

        return (GetEmailResponse) GenericRequestUtil.get(
                RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
                new RequestAreaApi()
            ).getResponseEntity();
    }

    public static Email getEmail(String identity) {
        url = url.concat(Constants.getNtsServiceHost() + "/emails/" + identity + "?key=" + Constants.getSecretKey());

        Map<String, String> headers = new HashMap<>();
        headers.put("ap-cloud-context", Constants.getNtsTargetCloudContext());

        return (Email) GenericRequestUtil.get(
                RequestEntity.init(url, Email.class).setHeaders(headers),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static Notifications getNotifications() {
        url = url.concat(Constants.getNtsServiceHost() + "/notifications?key=" + Constants.getSecretKey());
        return (Notifications) GenericRequestUtil.get(
                RequestEntity.init(url, Notifications.class),
                new RequestAreaApi()
        ).getResponseEntity();
    }
}
