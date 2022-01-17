package com.apriori.nts.apicalls;

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
import com.apriori.utils.token.TokenUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;

public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String cloudContext = new TokenUtil().getAuthTargetCloudContext(UserUtil.getUser());
    private static SendEmail sendEmail;

    private static Map<String, String> headers = new HashMap<String, String>() {{
            put("ap-cloud-context", cloudContext);
        }};

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
     * @param subject      Email subject
     * @param filename     Email Attachment
     * @param emailContent Email content
     * @return Service response
     */
    public static ResponseWrapper<SendEmail> sendEmailWithAttachment(String subject, String filename, String emailContent) {
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
    public static ResponseWrapper<SendEmail> sendEmail(String subject, String emailContent) {
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
    public static ResponseWrapper<EmailsItems> getEmails() {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAILS, EmailsItems.class)
            .headers(headers);

        return HTTPRequest.build(requestEntity).get();
    }

    public static ResponseWrapper<EmailsItems> getEmailsByIdentity(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAILS_BY_ID, EmailsItems.class)
                .inlineVariables(identity)
                .headers(headers);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Call the GetEmails service endpoint
     *
     * @param identity The identity of the email to retrieve
     * @return Service response
     */
    public static ResponseWrapper<Email> getEmail(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_EMAIL_BY_ID, Email.class)
            .inlineVariables(identity)
            .headers(headers);

        return HTTPRequest.build(requestEntity).get();
    }
}