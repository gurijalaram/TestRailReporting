package utils;

import com.apriori.utils.FileResourceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_USER_NAME_KEY = System.getProperty("username");
    public static final String DEFAULT_PASSWORD_KEY = System.getProperty("password");
    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cic-qa-21.1";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;

    private static final String ROW_RANGE = "Rows1-25";

    // Login Credentials
    public static final String USER_EMAIL = "dmorrow@apriori.com";
    public static final String USER_PASSWORD = "Q@1testerr";
    public static final String SECOND_USER_EMAIL = "";
    public static final String SECOND_USER_PASSWORD = "";

    //New Workflow Connector - need to be connected to PLM
    public static final String NWF_CONNECTOR = "automation - aP Internal - WC";

    //Default part id
    public static final String DEFAULT_PART_ID = "12345";

    //Default wait in millisecond
    public static final int DEFAULT_WAIT = 3000;

    //Default worklflow list size
    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final String MAX_PAGE_SIZE_25_TEXT = "25 rows per page";
    public static final String MAX_PAGE_SIZE_100_TEXT = "100 rows per page";
    public static final String DEFAULT_ROW_RANGE = "Rows1 - 25";
    public static final String NEXT_ROW_RANGE = "Rows26 - 50";

    //Default worflow names
    public static final String DEFAULT_WORKFLOW_NAME = "0   0   0   0   0 Automation Workflow";
    public static final String DEFAULT_EDITED_WORKFLOW_NAME = "0   0   0   0   0 Automation Workflow - Updated";
    public static final String DEFAULT_NAME_WITH_NUMBER = "0   0   0   1 Worflow Automation";
    public static final String DEFAULT_NAME_UPPER_CASE = "0   0   0   A Upper Workflow Automation";
    public static final String DEFAULT_NAME_LOWER_CASE = "0   0   0   a Lower Workflow Automation";

    public static final String DEFAULT_WORKFLOW_DESCRIPTION = "This workflow was created by automation";

    //Workflow field boundaries
    public static final int MAXIMUM_WORKFLOW_NAME_CHARACTERS = 64;
    public static final int MINIMAL_WORKFLOW_NAME_CHARACTERS = 1;
    public static final int MAXIMUM_WORKFLOW_DESCRIPTION_CHARACTERS = 255;

    //Query Definition
    public static final String DEFAULT_RULE_FILTER = "partNumber";
    public static final String DEFAULT_RULE_OPERATOR = "not_equal";

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
}
