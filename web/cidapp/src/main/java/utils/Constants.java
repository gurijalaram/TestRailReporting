package utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cidapp-qa";
    public static String environment;
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

    static {
        environment = environment == null ? System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE) : System.setProperty(DEFAULT_ENVIRONMENT_KEY, environment);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get default url
     * @return string
     */
    public static String getDefaultUrl() {
        return PROPERTIES.getProperty("url.default");
    }
}
