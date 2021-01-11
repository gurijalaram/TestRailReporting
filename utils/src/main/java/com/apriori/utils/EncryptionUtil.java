package com.apriori.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static String decryptFile(String key, InputStream inputStream) throws Exception {
        return crypto(Cipher.DECRYPT_MODE, key, inputStream, null);
    }

    private static String crypto(int cipherMode, String key, InputStream inputStream,
                               File outputFile) throws Exception {


        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);


            byte[] inputBytes = IOUtils.toByteArray(inputStream);
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            if (cipherMode == Cipher.DECRYPT_MODE) {
                String credentials = new String(outputBytes, StandardCharsets.UTF_8);
                return credentials;
            } else {
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(outputBytes);
                inputStream.close();
                outputStream.close();
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }
}