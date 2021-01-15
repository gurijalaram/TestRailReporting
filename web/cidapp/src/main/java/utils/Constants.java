package utils;

import com.apriori.utils.FileResourceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cidapp-qa";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

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

        return baseUrl.concat(PROPERTIES.getProperty("url.additional"));
    }
}
