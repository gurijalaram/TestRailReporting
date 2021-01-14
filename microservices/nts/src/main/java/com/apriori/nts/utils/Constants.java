package com.apriori.nts.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "nts";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    private static String ntsServiceHost;
    private static String ntsEmailRecipientAddress;
    private static String ntsEmailSubject;
    private static String ntsEmailContent;
    private static String ntsEmailAttachment;
    private static String secretKey;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            PROPERTIES.list(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get default url
     *
     * @return string
     */
    public static String getDefaultUrl() {
        baseUrl = System.getProperty(DEFAULT_BASE_URL_KEY) == null ? PROPERTIES.getProperty("url.default") : System.getProperty(DEFAULT_BASE_URL_KEY);
        System.setProperty("baseUrl", baseUrl);

        return baseUrl;
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getNtsServiceHost() {
        return ntsServiceHost = System.getProperty("ntsServiceHost") == null ? PROPERTIES.getProperty("nts.service.host") : System.getProperty("ntsServiceHost");
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("ntsSecretKey") == null ? PROPERTIES.getProperty("nts.secret.key") : System.getProperty("ntsSecretKey");
    }

    /**
     * Get recipient address
     *
     * @return string
     */
    public static String getNtsEmailRecipientAddress() {
        return ntsEmailRecipientAddress = System.getProperty("ntsRecipientAddress") == null ? PROPERTIES.getProperty("nts.email.recipientAddress") : System.getProperty("ntsRecipientAddress");
    }

    /**
     * Get email subject
     *
     * @return string
     */
    public static String getNtsEmailSubject() {
        return ntsEmailSubject = System.getProperty("ntsEmailSubject") == null ? PROPERTIES.getProperty("nts.email.subject") : System.getProperty("ntsEmailSubject");
    }

    /**
     * Get email subject
     *
     * @return string
     */
    public static String getNtsEmailContent() {
        return ntsEmailContent = System.getProperty("ntsEmailContent") == null ? PROPERTIES.getProperty("nts.email.content") : System.getProperty("ntsEmailContent");
    }

    /**
     * Get email attachment
     *
     * @return string
     */
    public static String getNtsEmailAttachment() {
        return ntsEmailAttachment = System.getProperty("ntsEmailAttachment") == null ? PROPERTIES.getProperty("nts.email.attachment") : System.getProperty("ntsEmailAttachment");
    }

    /**
     * Get target cloud context
     *
     * @return string
     */
    public static String getTargetCloudContext() {
        return ntsEmailAttachment = System.getProperty("ntsTargetCloudContext") == null ? PROPERTIES.getProperty("nts.auth.targetCloudContext") : System.getProperty("ntsTargetCloudContext");
    }
}
