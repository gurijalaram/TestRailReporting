package utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String SAVED_CONFIG_NAME = "Saved Config";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTING_HELP_URL = "https://help.jaspersoft.com/Default";
    public static final String PRIVACY_POLICY_URL = "https://www.apriori.com/privacy-policy";
    public static final String PISTON_ASSEMBLY_CID_NAME = "PISTON_ASSEMBLY";
    public static final String DTC_METRICS_FOLDER = "DTC Metrics";
    public static final String GENERAL_FOLDER = "General";
    public static final String PUBLIC_WORKSPACE = "Public";
    public static final String PRIVATE_WORKSPACE = "Private";
    public static final String ASSEMBLY_STRING = "[assembly]";
    public static final String PART_NAME_INITIAL_EXPECTED_MACHINING_DTC = "PMI_SYMMETRYCREO (Initial) ";
    public static final String PART_NAME_EXPECTED_MACHINING_DTC = "PMI_FLATNESSCREO (Initial)";
    public static final String CASTING_DIE_SAND_NAME = String.format(
        "%s, %s",
        ProcessGroupEnum.CASTING_DIE.getProcessGroup(),
        ProcessGroupEnum.CASTING_SAND.getProcessGroup()
    );
    public static final String STOCK_MACHINING_TWO_MODEL_NAME = String.format(
        "%s, %s",
        ProcessGroupEnum.STOCK_MACHINING.getProcessGroup(),
        ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
    );
    public static final String DEFAULT_SCENARIO_NAME = "Initial";
    public static final String PART_SCENARIO_TYPE = "Part";
    public static final String ASSEMBLY_SCENARIO_TYPE = "Assembly";
    public static final String COMPARISON_SCENARIO_TYPE = "Comparison";
    public static final String FAILED_LOGIN_MESSAGE = "We're sorry, something went wrong when attempting to log in.";
    public static final String FORGOT_PWD_MESSAGE = "If the supplied email address is valid, you will receive an " +
        "email shortly with instructions on resetting your password. If you did not receive an email and still " +
        "require assistance, please send an email to support@apriori.com.";
    public static final String EMPTY_FIELDS_MESSAGE = "Can't be blank";
    public static final String INVALID_ERROR_MESSAGE = "Invalid";
    public static final String WARNING_TEXT = "This field is mandatory so you must enter data.";

    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cir-qa";
    public static final String ENVIRONMENT = System.getProperty(DEFAULT_ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_VALUE);
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;

    static {
        System.setProperty(DEFAULT_ENVIRONMENT_KEY, ENVIRONMENT);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile(DEFAULT_ENVIRONMENT_VALUE.concat(".properties"));

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
        return PROPERTIES.getProperty("url.default").concat(PROPERTIES.getProperty("url.additional"));
    }

    /**
     * Get default url
     * @return string
     */
    public static String getCidURL() {
        return PROPERTIES.getProperty("url.default").concat(PROPERTIES.getProperty("url.additional.cid"));
    }
}
