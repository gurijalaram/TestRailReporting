package utils;

import com.apriori.utils.FileResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String LOGOUT_HEADER = "CI DESIGN AUTOMATION";
    public static final String REPORTS_LAST_SUFFIX = "flow.html?_flowId=homeFlow";
    public static final String CIA_USER_GUIDE_URL_SUBSTRING = "CI_ADMIN_USER_GUIDE";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_ONE = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_TWO = "CIA_UG";
    public static final String CIA_USER_GUIDE_TITLE = "Cost Insight Admin\nUser Guide";
    public static final String SCENARIO_EXPORT_CHAPTER_PAGE_TITLE = "2 Scenario and System Data Exports";
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cia-qa";
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
    public static String getBaseUrl() {
        return PROPERTIES.getProperty("url.default");
    }

    /**
     * Get default url
     * @return string
     */
    public static String getDefaultUrl() {
        return PROPERTIES.getProperty("url.default").concat(PROPERTIES.getProperty("url.additional.admin"));
    }
}
