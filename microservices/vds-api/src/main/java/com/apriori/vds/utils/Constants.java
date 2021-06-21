package com.apriori.vds.utils;

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

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "vds-int-api";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

    public static String environment;

    private static String secretKey;
    private static String atsServiceHost;
    private static String apiUrl;
    private static String apUserContext;

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
    public static String getApiUrl() {
        return apiUrl = System.getProperty("vdsApiUrl") == null ? PROPERTIES.getProperty("vds.api.url") : System.getProperty("vdsApiUrl");
    }


    /**
     * Get secret key
     *
     * @return string
     */
    public static String getSecretKey() {
        return secretKey = System.getProperty("vdsSecretKey") == null ? PROPERTIES.getProperty("vds.secret.key") : System.getProperty("vdsSecretKey");
    }

    /**
     * Get service host
     *
     * @return string
     */
    public static String getAtsServiceHost() {
        return atsServiceHost = System.getProperty("atsServiceHost") == null ? PROPERTIES.getProperty("ats.service.host") : System.getProperty("atsServiceHost");
    }


    /**
     * Get ap-user-context
     *
     * @return string
     */
    public static String getApUserContext() {
        return apUserContext = System.getProperty("apUserContext") == null ? PROPERTIES.getProperty("vds.ap.user.context") : System.getProperty("apUserContext");
    }
}
