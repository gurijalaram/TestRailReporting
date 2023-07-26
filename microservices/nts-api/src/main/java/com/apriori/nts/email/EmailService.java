package com.apriori.nts.email;

import com.apriori.FileResourceUtil;
import com.apriori.authorization.AuthorizationUtil;
import com.apriori.authorization.response.EmailMessage;
import com.apriori.email.GraphEmailService;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.nts.entity.response.Email;
import com.apriori.nts.entity.response.EmailsItems;
import com.apriori.nts.entity.response.SendEmail;
import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.nts.utils.EmailSetup;
import com.apriori.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EmailService {

    private final String cloudContext = new AuthorizationUtil().getAuthTargetCloudContext(UserUtil.getUser());
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
        EmailMessage emailMessage = GraphEmailService.searchEmailMessage("$search," + subject);
        try {
            int count = 0;
            int defaultTimeout = 12;
            while (count <= defaultTimeout) {
                if (emailMessage.getSubject().toLowerCase().equals(subject.toLowerCase())) {
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
}