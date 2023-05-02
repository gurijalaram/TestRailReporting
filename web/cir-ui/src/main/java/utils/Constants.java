package utils;

import com.apriori.utils.enums.ProcessGroupEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Constants {
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String SAVED_CONFIG_NAME = "Saved Config";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTING_HELP_URL = "https://help.jaspersoft.com/Default";
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
    public static final String OTHER_SCENARIO_NAME = "sand casting";
    public static final String COST_NAME = "Cost";
    public static final String ANNUALISED_VALUE = "Annualized";
    public static final String PERCENT_VALUE = "Percent";
    public static final String NAME_TO_SELECT = "bhegan";
    public static final String WARNING_TEXT = "This field is mandatory so you must enter data.";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String API_REPORTS_PATH = "schemas/api-test-reports-schemas";
    public static Map<String, String> INPUT_CONTROL_NAMES = new HashMap<String, String>() {{
            put("Cost Metric", "costMetric");
            put("Mass Metric", "massMetric");
            put("Process Group", "processGroup");
            put("DTC Score", "dtcScore");
            put("Minimum Annual Spend", "annualSpendMin");
            put("Sort Order", "sortOrder");
            put("Currency", "currencyCode");
        }};
    public static final List<String> PART_NAMES_FOR_INPUT = Arrays.asList(
        "40137441.MLDES.0002 (Initial)",
        "CYLINDER HEAD (Initial)",
        "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)",
        "40128483.MLDES.0001 (Initial)",
        "GEAR HOUSING (Initial)",
        "40089252.MLDES.0004.REDRAW (Initial)",
        "DU100024720_G (Initial)",
        "40116211.MLDES.0004 (Initial)",
        "BARCO_R8552931 (Initial)",
        "DTCCASTINGISSUES (sand casting)",
        "DTCCASTINGISSUES (Initial)",
        "1205DU1017494_K (Initial)",
        "OBSTRUCTED MACHINING (Initial)",
        "B2315 (Initial)",
        "DU600051458 (Initial)",
        "DU200068073_B (Initial)",
        "E3-241-4-N (Initial)",
        "BARCO_R8761310 (Initial)",
        "84C602281P1_D (Initial)",
        "40090936.MLDES.0004 (Initial)",
        "CASE_08 (Initial)",
        "BARCO_R8762839_ORIGIN (Initial)",
        "C192308 (Initial)",
        "40144122.MLDES.0002 (Initial)",
        "2980123_CLAMP (Bulkload)",
        "1271576 (Bulkload)",
        "AP_BRACKET_HANGER (Initial)",
        "DS73-F04604-PIA1 (Bulkload)",
        "2980123_LINK (Bulkload)",
        "2551580 (Bulkload)",
        "0903238 (Bulkload)",
        "1684402TOP_BRACKET (Bulkload)",
        "2840020_BRACKET (Bulkload)",
        "BRACKET_SHORTENED_ISSUES (Initial)",
        "3575137 (Bulkload)",
        "BRACKET_V1 (Initial)",
        "BRACKET_V2 (Initial)",
        "BRACKET_SHORTENED (Initial)",
        "BRACKET_V1_HEMS (Initial)",
        "BRACKET_V3 (Initial)",
        "BRACKET_V4 (Initial)",
        "3575136 (Bulkload)",
        "1100149 (Initial)",
        "1684443_OUTRIGGER_CAM (Initial)",
        "3574715 (Initial)",
        "3574688 (Initial)"
    );
}
