package com.utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cid-qa";
    public static final String EVALUATE_TOOLTIP_TEXT = "There is no active scenario.\n- Select one in Explore\nOR\n- Use New to create a new one.";
    public static final String COMPARE_TOOLTIP_TEXT = "There is no active comparison.\n- Select one in Explore\nOR\n- Use New to create a new one.";
    public static final String ARROW_DOWN = "arrow_down";
    public static final String PAGE_DOWN = "page_down";
    public static final String HORIZONTAL_SCROLL = "horizontal_scroll";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE) : System.setProperty(DEFAULT_ENVIRONMENT_KEY, environment);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(environment.concat(".properties"));

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
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
        return baseUrl.concat(PROPERTIES.getProperty("url.additional"));
    }
}
