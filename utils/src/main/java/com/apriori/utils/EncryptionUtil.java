package com.apriori.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encryptFile(String key, File inputFile, File outputFile)
            throws Exception {
        crypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static String decryptFile(String key, File inputFile)
            throws Exception {
        return crypto(Cipher.DECRYPT_MODE, key, inputFile, null);
    }

    private static String crypto(int cipherMode, String key, File inputFile,
                               File outputFile) throws Exception {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
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
            logger.error(e.getMessage());
        }

        return null;
    }
}
