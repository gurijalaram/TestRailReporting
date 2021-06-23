package com.utils;

import com.apriori.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "qa-21-1";
    public static final List<String> INPUT_VALUES = Arrays.asList("annual volume", "batch size", "material", "production life", "component name", "description", "notes",
        "scenario name", "tolerance count", "fully burdened cost", "material cost", "piece part cost", "total capital investment",
        "cycle time", "finish mass", "process routing", "utilization");
    public static final List<String> TOGGLE_VALUES = Arrays.asList("cad connected", "locked", "published");
    public static final List<String> DATE_VALUES = Arrays.asList("created at", "last updated at");
    public static final List<String> TYPE_INPUT_VALUES = Arrays.asList("process group", "vpe", "assignee", "component type", "cost maturity", "created by", "last updated by",
        "state", "status", "dfm");
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;

    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("cidapp-ui-" + environment + ".properties");

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
            String properties = PROPERTIES.stringPropertyNames().stream()
                .map(key -> key + "=" + PROPERTIES.getProperty(key) + "\n")
                .collect(Collectors.joining());
            log.info(String.format("Listing properties for '%s' " + "\n" + "%s", INPUT_STREAM, properties));
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
