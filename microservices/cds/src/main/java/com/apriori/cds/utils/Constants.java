package com.apriori.cds.utils;

import com.apriori.utils.FileResourceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cds";
    private static final File INPUT_STREAM;
    private static final Properties PROPERTIES = new Properties();
    public static String environment;
    private static String cdsIdentityUser;
    private static String cdsIdentityRole;
    private static String cdsIdentityCustomer;
    private static String cdsIdentityApplication;
    private static String baseUrl;
    private static String serviceUrl;
    private static String serviceHost;
    private static String secretKey;
    private static String protocol;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            LOGGER.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
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

        return baseUrl.concat(PROPERTIES.getProperty("url.additional"));
    }

    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("cdsSecretKey") == null ? PROPERTIES.getProperty("cds.secret.key") : System.getProperty("cdsSecretKey");
    }

    /**
     * Get identity user
     *
     * @return string
     */
    public static String getCdsIdentityUser() {
        return cdsIdentityUser = System.getProperty("cdsIdentityUser") == null ? PROPERTIES.getProperty("cds.identity.user") : System.getProperty("cdsIdentityUser");
    }

    /**
     * Get identity role
     *
     * @return string
     */
    public static String getCdsIdentityRole() {
        return cdsIdentityRole = System.getProperty("cdsIdentityRole") == null ? PROPERTIES.getProperty("cds.identity.role") : System.getProperty("cdsIdentityRole");
    }

    /**
     * Get customer identity
     *
     * @return string
     */
    public static String getCdsIdentityCustomer() {
        return cdsIdentityCustomer = System.getProperty("cdsIdentityCustomer") == null ? PROPERTIES.getProperty("cds.identity.customer") : System.getProperty("cdsIdentityCustomer");
    }

    /**
     * Get identity application
     *
     * @return string
     */
    public static String getCdsIdentityApplication() {
        return cdsIdentityApplication = System.getProperty("cdsIdentityApplication") == null ? PROPERTIES.getProperty("cds.identity.application") : System.getProperty("cdsIdentityApplication");
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getServiceHost() {
        return serviceHost = System.getProperty("cdsServiceHost") == null ? PROPERTIES.getProperty("cds.service.host") : System.getProperty("cdsServiceHost");
    }

    /**
     * Get protocol
     *
     * @return string
     */
    public static String getProtocol() {
        return protocol = System.getProperty("cdsProtocol") == null ? PROPERTIES.getProperty("cds.protocol") : System.getProperty("cdsProtocol");
    }

    /**
     * Builds the service url
     *
     * @return string
     */
    public static String getServiceUrl() {
        return serviceUrl = getProtocol().concat(getServiceHost()).concat("/%s?key=").concat(getSecretKey());
    }

    /**
     * Get signin url
     * @return string
     */
    public static String getSignInUrl() {
        return PROPERTIES.getProperty("signin.url");
    }

    /**
     * Get signin certificate
     * @return string
     */
    public static String getSignInCert() {
        return PROPERTIES.getProperty("signin.cert");
    }

    /**
     * Get user id
     * @return string
     */
    public static String getUserId() {
        return PROPERTIES.getProperty("cds.user.id");
    }

    /**
     * Get email
     * @return string
     */
    public static String getEmail() {
        return PROPERTIES.getProperty("cds.email");
    }

    /**
     * Get name
     * @return string
     */
    public static String getName() {
        return PROPERTIES.getProperty("cds.name");
    }

    /**
     * Get given name
     * @return string
     */
    public static String getGivenName() {
        return PROPERTIES.getProperty("cds.givenName");
    }

    /**
     * Get family name
     * @return string
     */
    public static String getFamilyName() {
        return PROPERTIES.getProperty("cds.familyName");
    }
}
