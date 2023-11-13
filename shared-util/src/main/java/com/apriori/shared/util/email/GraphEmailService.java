package com.apriori.shared.util.email;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailResponse;
import com.apriori.shared.util.utils.KeyValueUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GraphEmailService {

    private static final int POLL_TIME = 15;
    private Credentials credentials = null;

    /**
     * Search email in connected email box that has attachments
     *
     * @param searchText email text to be searched
     * @return EmailMessage
     */
    public static EmailMessage searchEmailMessageWithAttachments(String searchText) {
        String[] emailParamValues = {"$search, \"" + searchText + "\"", "hasAttachments[eq], true"};

        RequestEntity requestEntity = RequestEntityUtil_Old.init(EmailEnum.EMAIL_MESSAGES, EmailResponse.class)
            .queryParams(new KeyValueUtil().keyValue(emailParamValues, ","))
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Bearer " + EmailConnection.getEmailAccessToken());
                }
            }).expectedResponseCode(HttpStatus.SC_OK);

        return trackEmailMessage(requestEntity, searchText);
    }

    /**
     * Search email in connected email box
     *
     * @param emailParamValues search parameters in key-pair comma seperated
     *                         (Example: "$search, \"ap-int12345\"", "hasAttachments[eq], true"
     * @return EmailMessage
     */
    // TODO z: fix it threads
    public static synchronized EmailMessage searchEmailMessage(String... emailParamValues) {
        QueryParams emailParamValue = new KeyValueUtil().keyValue(emailParamValues, ",");

        // RequestEntity requestEntity = RequestEntityUtil_Old.init(EmailEnum.EMAIL_MESSAGES, EmailResponse.class)

        RequestEntity requestEntity = new RequestEntity()
            .endpoint(EmailEnum.EMAIL_MESSAGES)
            .returnType(EmailResponse.class)
            .queryParams(emailParamValue)
            .headers(new HashMap<>() {
                {
                    put("Authorization", "Bearer " + EmailConnection.getEmailAccessToken());
                }
            }).expectedResponseCode(HttpStatus.SC_OK);

        return trackEmailMessage(requestEntity, emailParamValue.get("$search"));
    }

    private static EmailMessage trackEmailMessage(RequestEntity requestEntity, String searchEmailText) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(15);
        do {
            try {
                TimeUnit.SECONDS.sleep(POLL_TIME);
                EmailResponse emailResponse = (EmailResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
                if (emailResponse.getValue().size() > 0) {
                    return emailResponse.getValue().get(0);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (LocalTime.now().isAfter(expectedFileArrivalTime));

        throw new RuntimeException(String.format("EMAIL NOT RECEIVED WITH SCENARIO NAME --- %s", searchEmailText));
    }
}