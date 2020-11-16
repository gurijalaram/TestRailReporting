package utils;

import com.apriori.utils.enums.ProcessGroupEnum;

public class Constants {

    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String SAVED_CONFIG_NAME = "Saved Config";
    public static final String DOMAIN_DESIGNER_URL_SUFFIX = String.format("%sdomaindesigner.html", REPORTS_URL_SUFFIX);
    public static final String REPORTING_HELP_URL = "https://help.jaspersoft.com/Default";
    public static final String PRIVACY_POLICY_URL = "https://www.apriori.com/privacy-policy";
    public static final String PISTON_ASSEMBLY_CID_NAME = "PISTON_ASSEMBLY";
    public static final String DTC_METRICS_FOLDER = "DTC Metrics";
    public static final String GENERAL_FOLDER = "General";
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
}
