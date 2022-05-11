package com.apriori.nts.utils;

import com.apriori.apibase.services.nts.objects.Credentials;
import com.apriori.utils.EncryptionUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.json.utils.JsonManager;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class EmailSetup {

    private  Credentials credentials = null;

    /**
     * Retrieve credentials and host url for the email account.
     */
    public  void getCredentials() {
        String key = "lygtvxdsesdfhind";
        InputStream credentialFile = FileResourceUtil.getResourceFileStream("crd");

        try {
            String content = EncryptionUtil.decryptFile(key, credentialFile);
            credentials = JsonManager.deserializeJsonFromString(content, Credentials.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public  String getHost() {
        return credentials.getHost();
    }

    public  String getUsername() {
        return credentials.getUsername();
    }

    public  String getPassword() {
        return credentials.getPassword();
    }


}


