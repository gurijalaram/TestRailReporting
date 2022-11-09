package com.apriori.nts.email;

import com.apriori.nts.entity.response.Email;
import com.apriori.nts.entity.response.EmailsItems;
import com.apriori.nts.entity.response.SendEmail;
import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.nts.utils.EmailSetup;
import com.apriori.utils.EmailUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;

@Slf4j
public class EmailService {

    private final String cloudContext = UserUtil.getUser().getCloudContext();
    private Map<String, String> headers = new HashMap<String, String>() {{
            put("ap-cloud-context", cloudContext);
        }};

    /**
     * Validates that an email has been sent by checking the target account
     *
     * @param subject Email subject
     * @return True, if the email exits
     */
    public Boolean validateEmail(String subject) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();
        try {
            int count = 0;
            int defaultTimeout = 12;
            while (count <= defaultTimeout) {
                Message[] messages = EmailUtil.getEmail(
                    emailSetup.getHost(),
                    emailSetup.getUsername(),
                    emailSetup.getPassword(),
                    emailSetup.getFolder()
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
            log.error(e.getMessage());
        }

        return false;
    }

    /**
     * Call the SendEmail endpoint
     *
     * @param subject      Email subject
     * @param filename     Email Attachment
     * @param emailContent Email content
     * @return Service response
     */
    public ResponseWrapper<SendEmail> sendEmailWithAttachment(String subject, String filename, String emailContent) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.POST_EMAIL, SendEmail.class)
            .headers(headers)
            .multiPartFiles(new MultiPartFiles()
                .use("data", FileResourceUtil.getResourceAsFile(filename))
                .use("recipientAddress", emailSetup.getUsername())
                .use("subject", subject)
                .use("content", emailContent)
            );

        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Call the SendEmail service endpoint
     *
     * @param subject      Email subject
     * @param emailContent Email content
     * @return Service response
     */
    public ResponseWrapper<SendEmail> sendEmail(String subject, String emailContent) {
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();

        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.POST_EMAIL, SendEmail.class)
            .headers(headers)
            .multiPartFiles(new MultiPartFiles()
                .use("recipientAddress", emailSetup.getUsername())
                .use("subject", subject)
                .use("content", emailContent)
            );

        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Call the GetEmail service endpoint
     *
     * @return Service response
     */
    public ResponseWrapper<EmailsItems> getEmails() {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAILS, EmailsItems.class)
            .headers(headers);

        return HTTPRequest.build(requestEntity).get();
    }

    public ResponseWrapper<EmailsItems> getEmailsByIdentity(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAILS_BY_ID, EmailsItems.class)
            .inlineVariables(identity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Call the GetEmails service endpoint
     *
     * @param identity The identity of the email to retrieve
     * @return Service response
     */
    public ResponseWrapper<Email> getEmail(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAIL_BY_ID, Email.class)
            .inlineVariables(identity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get email with Attachment by checking the target email account
     *
     * @param subject Email subject
     * @return True, if the email exits
     */
    public EmailData getEmailMessageData(String subject, String scenarioName) {
        EmailData emailData = new EmailData();
        try {
            Message message = this.getEmailMessage(subject, scenarioName);
            if (message != null) {
                emailData.setAttachments(this.getAttachmentsFromMessage(message));
                emailData.setContent(this.getTextFromMessage(message));
                emailData.setSubject(message.getSubject());
                emailData.setMessage(message);
            } else {
                log.debug("No Emails Found !!!!");
            }
        } catch (Exception e) {
            log.error("REPORT EMAIL WAS NOT RECEIVED!!!" + e.getMessage());
        }
        return emailData;
    }

    /**
     * Validates that an email has been sent by checking the target account
     *
     * @param subject Email subject
     * @return True, if the email exits
     */
    private Message getEmailMessage(String subject, String scenarioName) {
        Message emailMessage = null;
        EmailSetup emailSetup = new EmailSetup();
        emailSetup.getCredentials();
        try {
            int count = 0;
            int defaultTimeout = 12;
            while (count <= defaultTimeout) {
                Message[] messages = EmailUtil.getEmail(
                    emailSetup.getHost(),
                    emailSetup.getUsername(),
                    emailSetup.getPassword(),
                    emailSetup.getFolder()
                );
                emailMessage = messages[messages.length - 1];
                if (emailMessage.getSubject().toLowerCase().equals(subject.toLowerCase())) {
                    return (getTextFromMessage(emailMessage).contains(scenarioName)) ? emailMessage : null;
                } else {
                    Thread.sleep(5000);
                }
                count += 1;
            }
        } catch (Exception e) {
            log.error("REPORT EMAIL NOT FOUND!!! " + e.getMessage());
        }
        return emailMessage;
    }

    /**
     * mark the emails for delete
     *
     * @param msg
     */
    public void markForDelete(Message msg) {
        log.debug("Marking emails for deletion - setting deleted flag to true");
        try {
            msg.setFlag(Flags.Flag.DELETED, true);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    /**
     * Searched the email body parts for attachments and downloads each attachment to a Byte Array Stream
     * to be available for a download request
     *
     * @param message
     * @return
     */
    private static List<File> getAttachmentsFromMessage(Message message) {
        List<File> attachments = new ArrayList<>();
        try {
            if (!message.getContentType().trim().toLowerCase().startsWith("multipart")) {
                return attachments;
            }
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    File file = FileResourceUtil.copyIntoTempFile(bodyPart.getInputStream(), "report", bodyPart.getFileName());
                    attachments.add(file);
                }
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return attachments;
    }

    /**
     * Get text from email Body message depending on mime type
     *
     * @param message
     * @return whole text from the email Body message depending on the MimeType
     */
    private static String getTextFromMessage(Message message) {
        String emailText = StringUtils.EMPTY;
        try {
            String content = message.getContentType().split(";")[0];
            log.debug("Mime Type is " + content);
            switch (content) {
                case "text/plain":
                case "text/html":
                    return message.getContent().toString();
                case "multipart/mixed":
                case "multipart/alternative":
                case "multipart/*":
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    emailText = getTextFromMimeMultipart(mimeMultipart);
                    break;
                default:
                    log.error(String.format("Unsupported mime type %s", content));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return emailText;
    }

    /**
     * get text from multipart
     *
     * @param mimeMultipart from BodyPart
     * @return Text from MimeMultipart
     */
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
        String result = StringUtils.EMPTY;
        try {
            int count = mimeMultipart.getCount();

            boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
            if (multipartAlt) {
                return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
            }
            if (new ContentType(mimeMultipart.getContentType()).match("multipart/mixed")) {
                return getTextFromBodyPart(mimeMultipart.getBodyPart(0));
            }
            result = StringUtils.EMPTY;
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                result += getTextFromBodyPart(bodyPart);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * get text from body part
     *
     * @param bodyPart from email body
     * @return text from Body Part
     */
    private static String getTextFromBodyPart(BodyPart bodyPart) {
        String result = StringUtils.EMPTY;
        try {
            if (bodyPart.isMimeType("text/plain")) {
                result = (String) bodyPart.getContent();
            } else if (bodyPart.isMimeType("text/html")) {
                result = (String) bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}