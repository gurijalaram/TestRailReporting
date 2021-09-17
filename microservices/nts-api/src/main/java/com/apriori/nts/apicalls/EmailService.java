package com.apriori.nts.apicalls;

import com.apriori.nts.entity.response.Email;
import com.apriori.nts.entity.response.GetEmailResponse;
import com.apriori.nts.entity.response.SendEmailResponse;
import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.nts.utils.EmailSetup;
import com.apriori.nts.utils.NtsUtil;
import com.apriori.utils.EmailUtil;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;

public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String cloudContext = PropertiesContext.get("${env}.auth_target_cloud_context");


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
     * @param baseUrl      Base url of the NTS Service
     * @param subject      Email subject
     * @param parameters
     * @param emailContent Email content
     * @return Service response
     */
    private static SendEmailResponse sendEmail(String baseUrl, String subject, Map<String, String> parameters, String emailContent) {
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
            smr = (SendEmailResponse) NtsUtil.postMultiPartFormData(url, params, SendEmailResponse.class, cloudContext);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return smr;

    }

    /**
     * Call the SendEmail service endpoint
     *
     * @param baseUrl      Base url of the NTS Service
     * @param subject      Email subject
     * @param emailContent Email content
     * @return Service response
     */
    public static SendEmailResponse sendEmail(String baseUrl, String subject, String emailContent) {
        return sendEmail(baseUrl, subject, null, emailContent);
    }

    /**
     * Call the GetEmail service endpoint
     *
     * @return Service response
     */
    public static GetEmailResponse getEmails() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(NTSAPIEnum.GET_EMAIL, GetEmailResponse.class)
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", cloudContext);
                }
            });

        return (GetEmailResponse)
            HTTP2Request.build(requestEntity).get().getResponseEntity();


//
//                return (GetEmailResponse) GenericRequestUtil.get(
//                    RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
//                    new RequestAreaApi()
//                ).getResponseEntity();
    }

    /**
     * Call the GetEmails service endpoint
     *
     * @param identity The identity of the email to retrieve
     * @return Service response
     */
    public static Email getEmail(String identity) {
        //        String url = String.format(baseUrl, "/" + identity);
        //
        //        Map<String, String> headers = new HashMap<>();
        //        headers.put("ap-cloud-context", cloudContext);

        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(NTSAPIEnum.GET_EMAIL_BY_ID, Email.class)
            .inlineVariables(identity)
            .headers(new HashMap<String, String>() {{
                    put("ap-cloud-context", cloudContext);
                }
            });

        return (Email)
            HTTP2Request.build(requestEntity).get().getResponseEntity();

        //
        //            GenericRequestUtil.get(
        //            RequestEntity.init(url, GetEmailResponse.class).setHeaders(headers),
        //            new RequestAreaApi()
        //        ).getResponseEntity();
    }
}