package com.apriori.shared.util.email;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.EmailTokenResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class EmailConnection {

    private static String emailAccessToken;
    private static final String EMAIL_SECRET_AWS_PARAMETER = "/qaautomation/emailAuthSecretKey";

    /**
     * Connects to test email server box using Microsoft Graph Client API and returns access token
     */
    private static String connectEmailServer() {
        Credentials graphClient = getCredentials();
        RequestEntity requestEntity = RequestEntityUtil_Old.init(EmailEnum.EMAIL_TOKEN, EmailTokenResponse.class).inlineVariables(graphClient.getTenantId()).headers(new HashMap<String, String>() {
            {
                put("Accept", "*/*");
                put("Content-Type", "application/x-www-form-urlencoded");
            }
        }).xwwwwFormUrlEncodeds(Collections.singletonList(new HashMap<String, String>() {
            {
                put("client_id", graphClient.getClientId());
                put("client_secret", graphClient.getClientSecret());
                put("grant_type", graphClient.getGrantType());
                put("scope", graphClient.getScope());
                put("username", graphClient.getUsername());
                put("password", graphClient.getPassword());
            }
        })).expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<EmailTokenResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        log.info(String.format("ACCESSTOKEN --- %s", responseWrapper.getResponseEntity().getAccess_token()));
        return responseWrapper.getResponseEntity().getAccess_token();
    }

    /**
     * Retrieve credentials and host url for the email account.
     */
    private static Credentials getCredentials() {
        String secretKeyInfo;
        try {
            secretKeyInfo = AwsParameterStoreUtil.getSystemParameter(EMAIL_SECRET_AWS_PARAMETER);
            if (secretKeyInfo.isEmpty()) {
                throw new RuntimeException(EMAIL_SECRET_AWS_PARAMETER + " Not found in AWS Parameter store");
            }
            return JsonManager.deserializeJsonFromString(secretKeyInfo, Credentials.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * get access token
     *
     * @return access token
     */
    public static synchronized String getEmailAccessToken() {
        if (null == emailAccessToken) {
            emailAccessToken = connectEmailServer();
        }

        return emailAccessToken;
    }
}