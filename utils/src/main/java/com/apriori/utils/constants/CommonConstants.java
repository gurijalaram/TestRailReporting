package com.apriori.utils.constants;



import java.util.logging.Level;

public class CommonConstants {

    public static final String DEFAULT_PROJECT_ID_VALUE = "177";
    public static final String DEFAULT_PROJECT_ID_KEY = "RUN_ID";

    public static final String DEFAULT_USER_NAME = "admin@apriori.com";
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_ACCESS_LEVEL = "admin";
    public static String RUN_ID = DEFAULT_PROJECT_ID_VALUE;

    public static final Level consoleLogLevel = Level.parse(PropertiesContext.get("global.console_log_level"));
    public static final String schemaBasePath = PropertiesContext.get("global.schema_base_path");
    public static final String REMOTE_SHARED_FOLDER = "Departments/Engineering/QA/Automation/GoNoGoTests";
    public static final String PLM_TEST_PARTS_CSV_FILE = "plm-test-parts.csv";
}