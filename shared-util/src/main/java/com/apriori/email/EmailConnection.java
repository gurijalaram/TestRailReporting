package com.apriori.email;

import com.apriori.FileResourceUtil;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.EncryptionUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.models.response.EmailTokenResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class EmailConnection {

    private static String emailAccessToken;

    /**
     * Connects to test email server box using Microsoft Graph Client API and returns access token
     */
    private static void connectEmailServer() {
        Credentials graphClient = getCredentials();
        RequestEntity requestEntity = RequestEntityUtil.init(EmailEnum.EMAIL_TOKEN, EmailTokenResponse.class).inlineVariables(graphClient.getTenantId()).headers(new HashMap<String, String>() {
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
        emailAccessToken = responseWrapper.getResponseEntity().getAccess_token();
    }

    /**
     * Retrieve credentials and host url for the email account.
     */
    private static Credentials getCredentials() {
        String key = "lygtvxdsesdfhind";
        InputStream credentialFile = FileResourceUtil.getResourceFileStream("emailAuthInfo");

        try {
            String content = EncryptionUtil.decryptFile(key, credentialFile);
            return JsonManager.deserializeJsonFromString(content, Credentials.class);
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
    public static String getEmailAccessToken() {
        if (null == emailAccessToken) {
            connectEmailServer();
        }
        return emailAccessToken;
    }
}