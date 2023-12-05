package com.apriori.nts.api.email;

import com.apriori.nts.api.enums.NTSAPIEnum;
import com.apriori.nts.api.models.response.Email;
import com.apriori.nts.api.models.response.EmailsItems;
import com.apriori.nts.api.models.response.SendEmail;
import com.apriori.nts.api.utils.EmailSetup;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.MultiPartFiles;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.AuthorizationUtil;
import com.apriori.shared.util.models.response.EmailMessage;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmailService {

    private final String cloudContext = new AuthorizationUtil().getAuthTargetCloudContext(UserUtil.getUser("admin"));
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

        RequestEntity requestEntity = RequestEntityUtil_Old.init(NTSAPIEnum.POST_EMAIL, SendEmail.class)
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

        RequestEntity requestEntity = RequestEntityUtil_Old.init(NTSAPIEnum.POST_EMAIL, SendEmail.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(NTSAPIEnum.GET_EMAILS, EmailsItems.class)
            .headers(headers);

        return HTTPRequest.build(requestEntity).get();
    }

    public ResponseWrapper<EmailsItems> getEmailsByIdentity(String identity) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(NTSAPIEnum.GET_EMAILS_BY_ID, EmailsItems.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(NTSAPIEnum.GET_EMAIL_BY_ID, Email.class)
            .inlineVariables(identity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }
}