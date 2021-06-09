package com.apriori.utils;

import com.apriori.utils.enums.ProcessGroupEnum;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author cfrith
 */
public class Base64EncoderUtil {
    /**
     * Encodes file to base64
     *
     * @param componentName - the component name
     * @param resourceFile  - the resource file
     * @return string
     */
    public static String encodeFileToBase64Binary(String componentName, String resourceFile) {
        byte[] encoded = new byte[0];
        try {
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(resourceFile), componentName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
