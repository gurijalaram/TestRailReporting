package utils;

import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Constants {

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
    public static final String ASSEMBLY_COMPONENT_TYPE = "assembly";
    public static final String PART_COMPONENT_TYPE = "part";
    public static final String ROLLUP_COMPONENT_TYPE = "rollup";
    public static final String DEFAULT_SCENARIO_NAME = "Initial";
    public static final String OTHER_SCENARIO_NAME = "sand casting";
    public static final String PART_SCENARIO_TYPE = "Part";
    public static final String ASSEMBLY_SCENARIO_TYPE = "Assembly";
    public static final String COST_NAME = "Cost";
    public static final String ANNUALISED_VALUE = "Annualized";
    public static final String PERCENT_VALUE = "Percent";
    public static final String PRIVACY_POLICY_STRING = "APRIORI TECHNOLOGIES, INC. PRIVACY POLICY";
    public static final String FAILED_LOGIN_MESSAGE = "Invalid credentials supplied. Could not login to JasperReports " +
            "Server.";
    public static final String FORGOT_PWD_MSG = "IF THE SUPPLIED EMAIL ADDRESS IS VALID, YOU WILL RECEIVE AN EMAIL "
            .concat("SHORTLY WITH INSTRUCTIONS ON RESETTING YOUR PASSWORD. IF YOU DID NOT RECEIVE AN EMAIL AND STILL ")
            .concat("REQUIRE ASSISTANCE, PLEASE SEND AN EMAIL TO SUPPORT@APRIORI.COM.");
    public static final String EMPTY_FIELDS_MESSAGE = "Can't be blank";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Invalid";
    public static final String NAME_TO_SELECT = "bhegan";
    public static final String WARNING_TEXT = "This field is mandatory so you must enter data.";
    public static Map<String, String> INPUT_CONTROL_NAMES = new HashMap<String, String>() {{
            put("Cost Metric", "costMetric");
            put("Mass Metric", "massMetric");
            put("Process Group", "processGroup");
            put("DTC Score", "dtcScore");
            put("Minimum Annual Spend", "annualSpendMin");
            put("Sort Order", "sortOrder");
        }};
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String API_REPORTS_PATH = "schemas/api-test-reports-schemas";
    public static Map<String, String> currencies = new HashMap<String, String>() {{
            put("GBP", CurrencyEnum.USD.getCurrency());
            put("USD", CurrencyEnum.GBP.getCurrency());
        }};
}
