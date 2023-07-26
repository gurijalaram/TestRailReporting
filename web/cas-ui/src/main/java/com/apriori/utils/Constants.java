package com.apriori.utils;

import com.apriori.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "qa";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;
    public static final String CAS_LICENSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <siteInfo objId=\"1\" class=\"com.fbc.datamodel.SiteInfo\"> <company value=\"%s\" /> <licenseModules objId=\"2\" class=\"java.util.TreeMap\"> <entry> <key class=\"java.lang.String\" value=\"Casting\" /> <value objId=\"3\" class=\"com.fbc.datamodel.LicenseModule\"> <expirationDate class=\"java.lang.Long\" value=\"2527510884000\" /> <name value=\"Casting\" /> <signatureData value=\"302C02141D465701A3A02AA8D4EC762C6BF5386F2F869C6002143798DABDAEF0D1EAB24ACF36ABA6F3EFCA80F2BC\" />  <siteInfo refId=\"1\" /> </value> </entry> <entry> <key class=\"java.lang.String\" value=\"Forging\" /> <value objId=\"4\" class=\"com.fbc.datamodel.LicenseModule\"> <expirationDate class=\"java.lang.Long\" value=\"2527510884000\" /> <name value=\"Forging\" /> <signatureData value=\"302C021439825EAFF126924A3D6B144C0C190D73E81B180E021423C8AE3F5DCA223F0B2F266A89A01096F83A5729\" /> <siteInfo refId=\"1\" /> </value> </entry> <entry> <key class=\"java.lang.String\" value=\"Cost Model Tool\" /> <value objId=\"5\" class=\"com.fbc.datamodel.LicenseModule\"> <expirationDate class=\"java.lang.Long\" value=\"2527510884000\" /> <name value=\"Cost Model Tool\" /> <signatureData value=\"302C0214233401AB58D0758E48B22543CF9CA75EE7E864DD021451635DD3FC885AE4EBEB52B2C12CF626F5532C48\" /> <siteInfo refId=\"1\" /> </value> </entry> <entry> <key class=\"java.lang.String\" value=\"aPriori USA\" /> <value objId=\"6\" class=\"com.fbc.datamodel.LicenseModule\"> <expirationDate class=\"java.lang.Long\" value=\"2527510884000\" /> <name value=\"aPriori China\" /> <signatureData value=\"302D02140482C7488F026A40F4EF14F4674605BDCF019AD902150091589FA7BD4F35F415000954C67BEAB7C18C5759\" /> <siteInfo refId=\"1\" /> </value> </entry> </licenseModules> <signatureData value=\"302C02150083EBAFB892CC3851268347BDBBAAB3F58C47B9E102137235095B5C10900B68FBFC40BDD784DA215514\" /> <siteId value=\"%s\" />  <userLicenses objId=\"8\" class=\"java.util.TreeMap\"> <entry> <key class=\"java.lang.String\" value=\"%s\" /> <value objId=\"8\" class=\"com.fbc.datamodel.UserLicense\"> <expirationDate class=\"java.lang.Long\" value=\"2527510884000\" /> <licenseId value=\"%s\" /> <licenseModuleNames value=\"Casting,Forging,Cost Model Tool,aPriori USA\" /> <licenseName value=\"Test subLicense\" /> <numPurchasedUsers value=\"3\" /> <numUsers value=\"3\" /> <signatureData value=\"302C02140CF810A73F7A8212478DF1955576516EE746058C02144DEA10E0A0A60F6211B2FFB8DA6F58D3B9ABF4CB\" /> <siteInfo refId=\"1\" /> </value> </entry> </userLicenses> <wsMode value=\"false\" /> </siteInfo>";
    public static final String INVALID_STRUCTURE_LICENSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <siteInfo objId=\"1\" class=\"com.fbc.datamodel.SiteInfo\"> <company value=\"%s\" /> <siteId value=\"%s\" />  <userLicenses objId=\"8\" class=\"java.util.TreeMap\"> <entry> <key class=\"java.lang.String\" value=\"%s\" /> <licenseName value=\"Test subLicense\" /> <numPurchasedUsers value=\"3\" /> <numUsers value=\"3\" /> <signatureData value=\"302C02140CF810A73F7A8212478DF1955576516EE746058C02144DEA10E0A0A60F6211B2FFB8DA6F58D3B9ABF4CB\" /> <siteInfo refId=\"1\" /> </value> </entry> </userLicenses> <wsMode value=\"false\" /> </siteInfo>";

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("cas-ui-" + environment + ".properties");

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "")
                .collect(Collectors.joining());
            log.info(String.format("Listing properties for '%s' " + "" + "%s", environment, properties));
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
}
