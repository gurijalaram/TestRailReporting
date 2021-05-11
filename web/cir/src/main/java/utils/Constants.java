package utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Constants {

    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String LOGOUT_HEADER = "CI DESIGN AUTOMATION";
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String SAVED_CONFIG_NAME = "Saved Config";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTING_HELP_URL = "https://help.jaspersoft.com/Default";
    public static final String PRIVACY_POLICY_URL = "https://www.apriori.com/privacy-policy";
    public static final String PISTON_ASSEMBLY_CID_NAME = "PISTON_ASSEMBLY";
    public static final String GENERAL_FOLDER = "General";
    public static final String SOURCING_FOLDER = "Sourcing";
    public static final String SOLUTIONS_FOLDER = "Solutions";
    public static final String CYCLE_TIME_FOLDER = "Cycle Time";
    public static final String DTC_METRICS_FOLDER = "DTC Metrics";
    public static final String CASTING_DTC_FOLDER = "Casting";
    public static final String MACHINING_DTC_FOLDER = "Machining";
    public static final String PlASTIC_DTC_FOLDER = "Plastic";
    public static final String SHEET_METAL_DTC_FOLDER = "Sheet Metal";
    public static final String DESIGN_TO_COST_FOLDER = "Design To Cost";
    public static final String TARGET_AND_QUOTED_COST_FOLDER = "Target And Quoted Cost";

    public static final String PUBLIC_WORKSPACE = "Public";
    public static final String PRIVATE_WORKSPACE = "Private";
    public static final String ASSEMBLY_STRING = "[assembly]";
    public static final String PART_NAME_INITIAL_EXPECTED_MACHINING_DTC = "PMI_SYMMETRYCREO (Initial) ";
    public static final String PART_NAME_EXPECTED_MACHINING_DTC = "PMI_FLATNESSCREO (Initial)";
    public static final List<String> INPUT_VALUES = Arrays.asList("annual volume", "batch size", "material", "production life", "component name", "description", "notes",
            "scenario name", "tolerance count", "fully burdened cost", "material cost", "piece part cost", "total capital investment",
            "cycle time", "finish mass", "process routing", "utilization");
    public static final List<String> TOGGLE_VALUES = Arrays.asList("cad connected", "locked", "published");
    public static final List<String> DATE_VALUES = Arrays.asList("created at", "last updated at");
    public static final List<String> TYPE_INPUT_VALUES = Arrays.asList("process group", "vpe", "assignee", "component type", "cost maturity", "created by", "last updated by",
            "state", "status", "dfm");
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
    public static final String COST_NAME = "Cost";
    public static final String ANNUALISED_VALUE = "Annualized";
    public static final String PERCENT_VALUE = "Percent";
    public static final String PRIVACY_POLICY_STRING = "APRIORI TECHNOLOGIES, INC. PRIVACY POLICY";
    public static final String FAILED_LOGIN_MESSAGE = "We're sorry, something went wrong when attempting to log in.";
    public static final String FORGOT_PWD_MSG = "We've just sent you an email to reset your password.";
    public static final String EMAIL_EMPTY_FIELDS_MESSAGE = "Email can't be blank";
    public static final String PASSWORD_EMPTY_FIELDS_MESSAGE = "Password can't be blank";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Email is invalid";
    public static final String NAME_TO_SELECT = "bhegan";
    public static final String WARNING_TEXT = "This field is mandatory so you must enter data.";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "cir-qa-21-1";

    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    public static String environment;
    private static String baseUrl;

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

    /**
     * Gets CID url
     *
     * @return String
     */
    public static String getCidUrl() {
        return PROPERTIES.getProperty("url.additional.cid");
    }
}
