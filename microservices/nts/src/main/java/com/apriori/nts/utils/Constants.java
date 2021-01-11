package com.apriori.nts.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "ats";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    private static String ntsServiceHost;
    private static String ntsEmailRecipientAddress;
    private static String ntsEmailSubject;
    private static String ntsEmailContent;
    private static String ntsEmailAttachment;

    static {
        System.setProperty(DEFAULT_ENVIRONMENT_KEY, ENVIRONMENT);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getNtsServiceHost() {
        return ntsServiceHost = ntsServiceHost == null ? PROPERTIES.getProperty("nts.service.host") : System.getProperty("ntsServiceHost");
    }

    /**
     * Get recipient address
     *
     * @return string
     */
    public static String getNtsEmailRecipientAddress() {
        return ntsEmailRecipientAddress = ntsEmailRecipientAddress == null ? PROPERTIES.getProperty("nts.email.recipientAddress") : System.getProperty("ntsRecipientAddress");
    }

    /**
     * Get email subject
     *
     * @return string
     */
    public static String getNtsEmailSubject() {
        return ntsEmailSubject = ntsEmailSubject == null ? PROPERTIES.getProperty("nts.email.subject") : System.getProperty("ntsEmailSubject");
    }

    /**
     * Get email subject
     *
     * @return string
     */
    public static String getNtsEmailContent() {
        return ntsEmailContent = ntsEmailContent == null ? PROPERTIES.getProperty("nts.email.content") : System.getProperty("ntsEmailContent");
    }

    /**
     * Get email attachment
     *
     * @return string
     */
    public static String getNtsEmailAttachment() {
        return ntsEmailAttachment = ntsEmailAttachment == null ? PROPERTIES.getProperty("nts.email.attachment") : System.getProperty("ntsEmailAttachment");
    }
}
