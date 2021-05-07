package com.apriori.nts.utils;

import com.apriori.apibase.services.nts.objects.Credentials;
import com.apriori.utils.EncryptionUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.json.utils.JsonManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class EmailSetup {
    private static final Logger logger = LoggerFactory.getLogger(EmailSetup.class);

    private  Credentials credentials = null;

    /**
     * Retrieve credentials and host url for the email account.
     */
    public  void getCredentials() {
        String key = "lygtvxdsesdfhind";
        InputStream credentialFile = FileResourceUtil.getResourceFileStream("crd");

        try {
            String content = EncryptionUtil.decryptFile(key, credentialFile);
            credentials = (Credentials) JsonManager.deserializeJsonFromString(content, Credentials.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
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


