package com.apriori.apibase.services.nts.utils;

import com.apriori.apibase.services.nts.objects.Credentials;
import com.apriori.utils.EncryptionUtil;
import com.apriori.utils.FileResourceUtil;

import com.apriori.utils.json.utils.JsonManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class EmailSetup {
    private static final Logger logger = LoggerFactory.getLogger(EmailSetup.class);

    private static Credentials credentials = null;

    public static void getCredentials() {
        String key = "lygtvxdsesdfhind";
        File credentialFile = FileResourceUtil.getLocalResourceFile("credentials");

        try {
            String content = EncryptionUtil.decryptFile(key, credentialFile);
            credentials = (Credentials) JsonManager.deserializeJsonFromString(content, Credentials.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public static String getAddress() {
        return credentials.getAddress();
    }

    public static String getUsername() {
        return credentials.getUsername();
    }

    public static String getPassword() {
        return credentials.getPassword();
    }


}


