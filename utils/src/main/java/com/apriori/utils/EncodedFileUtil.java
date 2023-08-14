package com.apriori.utils;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author cfrith
 */
public class EncodedFileUtil {
    /**
     * Encodes file to base64
     *
     * @param componentName - the component name
     * @param processGroup  - the resource file
     * @return string
     */
    public static String encodeFileFromCloudToBase64Binary(String componentName, ProcessGroupEnum processGroup) {
        byte[] encoded;
        try {
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(FileResourceUtil.getCloudFile(processGroup, componentName)));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to encode request file: " + componentName);
        }
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
