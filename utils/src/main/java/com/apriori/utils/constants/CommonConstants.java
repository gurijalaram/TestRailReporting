package com.apriori.utils.constants;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class CommonConstants {

    public static final String DEFAULT_PROJECT_ID_VALUE = "177";
    public static final String DEFAULT_PROJECT_ID_KEY = "RUN_ID";

    public static final String DEFAULT_USER_NAME = "admin@apriori.com";
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_ACCESS_LEVEL = "admin";

    public static String RUN_ID = DEFAULT_PROJECT_ID_VALUE;

    private static String cisPartIdentity;

    private static String csvFile;
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

    static {

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("common.properties");

        try {
            PROPERTIES.load(new FileInputStream(INPUT_STREAM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Level consoleLogLevel = Level.parse(PROPERTIES.getProperty("console.log.level"));
    public static final String schemaBasePath = PROPERTIES.getProperty("schema.base.path");

    /**
     * Get true/false value of whether to use different user
     * @return string
     */
    public static String getUseDifferentUser() {
        return PROPERTIES.getProperty("different.users");
    }

    /**
     * Get csv file to use
     * @return string
     */
    public static String getCsvFile() {
        return csvFile = csvFile == null ? System.getProperty("csvFile", "onprem-users.csv") : System.getProperty("csvFile");
    }

    public static String getCisPartIdentity() {
        if (cisPartIdentity == null) {
            cisPartIdentity = System.getProperty("cisPartIdentity", PROPERTIES.getProperty("cis.part.identity"));
        }

        return cisPartIdentity;
    }
}