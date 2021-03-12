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

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "int";
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);
    private static final File INPUT_STREAM;
    private static final Properties PROPERTIES = new Properties();
    public static String environment;
    private static String userIdentity;
    private static String cdsIdentityRole;
    private static String aPrioriInternalCustomerIdentity;
    private static String apProApplicationIdentity;
    private static String baseUrl;
    private static String serviceUrl;
    private static String serviceHost;
    private static String secretKey;
    private static String protocol;
    private static String apProductionDeploymentIdentity;
    private static String apCoreInstallationIdentity;
    private static String apcloudHomeApplicationIdentity;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            logger.info(String.format("Listing properties for '%s' " + "\n" + "%s", environment, properties));
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
    public static String getUserIdentity() {
        return userIdentity = System.getProperty("userIdentity") == null ? PROPERTIES.getProperty("automation.user.identity") : System.getProperty("userIdentity");
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
    public static String getAPrioriInternalCustomerIdentity() {
        return aPrioriInternalCustomerIdentity = System.getProperty("aPrioriInternalCustomerIdentity") == null ? PROPERTIES.getProperty("aPrioriInternal.customer.Identity") : System.getProperty("aPrioriInternalCustomerIdentity");
    }

    /**
     * Get identity application
     *
     * @return string
     */
    public static String getApProApplicationIdentity() {
        return apProApplicationIdentity = System.getProperty("apProApplicationIdentity") == null ? PROPERTIES.getProperty("appro.application.identity") : System.getProperty("apProApplicationIdentity");
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
     * Get apriori production deployment identity
     *
     * @return string
     */
    public static String getApProductionDeploymentIdentity() {
        return apProductionDeploymentIdentity = System.getProperty("aprioriProductionDeploymentIdentity") == null ? PROPERTIES.getProperty("apriori.production.deployment.identity") : System.getProperty("aprioriProductionDeploymentIdentity");
    }

    /**
     * Get apriori core installation identity
     *
     * @return string
     */
    public static String getApCoreInstallationIdentity() {
        return apCoreInstallationIdentity = System.getProperty("aprioriCoreInstallationIdentity") == null ? PROPERTIES.getProperty("apriori.coreServices.installation.identity") : System.getProperty("aprioriCoreInstallationIdentity");
    }

    /**
     * Get apriori cloud home application identity
     *
     * @return string
     */
    public static String getApCloudHomeApplicationIdentity() {
        return apcloudHomeApplicationIdentity = System.getProperty("aprioriCloudHomeApplicationIdentity") == null ? PROPERTIES.getProperty("apriori.cloud.home.application.identity") : System.getProperty("aprioriCloudHomeApplicationIdentity");
    }

    /**
     * Get license
     *
     * @return string
     */
    public static String getLicense() {
        return PROPERTIES.getProperty("cds.license");
    }

    /**
     * Get license template
     *
     * @return string
     */
    public static String getLicenseTemplate() {
        return PROPERTIES.getProperty("cds.license.template");
    }

    /**
     * Get signin url
     *
     * @return string
     */
    public static String getSignInUrl() {
        return PROPERTIES.getProperty("signin.url");
    }

    /**
     * Get signin certificate
     *
     * @return string
     */
    public static String getSignInCert() {
        return PROPERTIES.getProperty("signin.cert");
    }

    /**
     * Get user id
     *
     * @return string
     */
    public static String getSamlNameIdentifier() {
        return PROPERTIES.getProperty("saml.attribute.nameidentifier");
    }

    /**
     * Get email
     *
     * @return string
     */
    public static String getSamlAttributeEmail() {
        return PROPERTIES.getProperty("saml.attribute.email");
    }

    /**
     * Get name
     *
     * @return string
     */
    public static String getSamlAttributeName() {
        return PROPERTIES.getProperty("saml.attribute.name");
    }

    /**
     * Get given name
     *
     * @return string
     */
    public static String getSamlAttributeGivenName() {
        return PROPERTIES.getProperty("saml.attribute.givenname");
    }

    /**
     * Get family name
     *
     * @return string
     */
    public static String getSamlAttributeFamilyName() {
        return PROPERTIES.getProperty("saml.attribute.familyname");
    }


}
