package com.apriori.utils.email;

import com.apriori.utils.KeyValueException;
import com.apriori.utils.email.response.EmailMessage;
import com.apriori.utils.email.response.EmailResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GraphEmailService {

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

        EmailResponse emailResponse = (EmailResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return emailResponse.getValue().get(0);
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

        EmailResponse emailResponse = (EmailResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return emailResponse.getValue().get(0);
    }
}