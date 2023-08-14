package com.apriori.email;

import com.apriori.exceptions.KeyValueException;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.models.response.EmailMessage;
import com.apriori.models.response.EmailResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        QueryParams queryParams = new QueryParams();
        String[] emailParamValues = {"$search, \"" + searchText + "\"", "hasAttachments[eq], true"};
        List<String[]> paramKeyValue = Arrays.stream(emailParamValues).map(o -> o.split(",")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(EmailEnum.EMAIL_MESSAGES, EmailResponse.class).queryParams(queryParams.use(paramMap)).headers(new HashMap<String, String>() {
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
    public static EmailMessage searchEmailMessage(String... emailParamValues) {
        QueryParams queryParams = new QueryParams();

        List<String[]> paramKeyValue = Arrays.stream(emailParamValues).map(o -> o.split(",")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(EmailEnum.EMAIL_MESSAGES, EmailResponse.class).queryParams(queryParams.use(paramMap)).headers(new HashMap<String, String>() {
            {
                put("Authorization", "Bearer " + EmailConnection.getEmailAccessToken());
            }
        }).expectedResponseCode(HttpStatus.SC_OK);

        return trackEmailMessage(requestEntity, paramMap.get("$search"));
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