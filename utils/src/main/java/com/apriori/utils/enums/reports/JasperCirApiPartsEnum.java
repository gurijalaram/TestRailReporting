package com.apriori.utils.enums.reports;

public enum JasperCirApiPartsEnum {

    P_40137441_MLDES_0002("40137441.MLDES.0002 (Initial)"),
    CYLINDER_HEAD("CYLINDER HEAD (Initial)"),
    JEEP_WJ_FRONT_BRAKE_DISC_99_04("JEEP WJ FRONT BRAKE DISC 99-04 (Initial)"),
    P_40128483_MLDES_0001("40128483.MLDES.0001 (Initial)"),
    GEAR_HOUSING("GEAR HOUSING (Initial)"),
    P_40089252_MLDES_0004_REDRAW("40089252.MLDES.0004.REDRAW (Initial)"),
    DU100024720_G("DU100024720_G (Initial)"),
    P_40116211_MLDES_0004("40116211.MLDES.0004 (Initial)"),
    BARCO_R8552931("BARCO_R8552931 (Initial)"),
    DTC_CASTING_ISSUES_SC("DTCCASTINGISSUES (sand casting)"),
    DTC_CASTING_ISSUES_I("DTCCASTINGISSUES (Initial)"),
    P_1205DU1017494_K("1205DU1017494_K (Initial)"),
    OBSTRUCTED_MACHINING("OBSTRUCTED MACHINING (Initial)"),
    B2315("B2315 (Initial)"),
    DU600051458("DU600051458 (Initial)"),
    DU200068073_B("DU200068073_B (Initial)"),
    E3_241_4_N("E3-241-4-N (Initial)"),
    BARCO_R8761310("BARCO_R8761310 (Initial)"),
    P_84C602281P1_D("84C602281P1_D (Initial)"),
    P_40090936_MLDES_0004("40090936.MLDES.0004 (Initial)"),
    CASE_08("CASE_08 (Initial)"),
    BARCO_R8762839_ORIGIN("BARCO_R8762839_ORIGIN (Initial)"),
    C192308("C192308 (Initial)"),
    P_40144122_MLDES_0002("40144122.MLDES.0002 (Initial)"),
    P_2980123_CLAMP("2980123_CLAMP (Bulkload)"),
    P_1271576("1271576 (Bulkload)"),
    AP_BRACKET_HANGER("AP_BRACKET_HANGER (Initial)"),
    DS73_F04604_PIA1("DS73-F04604-PIA1 (Bulkload)"),
    P_2980123_LINK("2980123_LINK (Bulkload)"),
    P_2551580("2551580 (Bulkload)"),
    P_0903238("0903238 (Bulkload)"),
    P_1684402_TOP_BRACKET("1684402TOP_BRACKET (Bulkload)"),
    P_2840020_BRACKET("2840020_BRACKET (Bulkload)"),
    BRACKET_SHORTENED_ISSUES("BRACKET_SHORTENED_ISSUES (Initial)"),
    P_3575137("3575137 (Bulkload)"),
    BRACKET_V1("BRACKET_V1 (Initial)"),
    BRACKET_V2("BRACKET_V2 (Initial)"),
    BRACKET_SHORTENED("BRACKET_SHORTENED (Initial)"),
    BRACKET_V1_HEMS("BRACKET_V1_HEMS (Initial)"),
    BRACKET_V3("BRACKET_V3 (Initial)"),
    BRACKET_V4("BRACKET_V4 (Initial)"),
    P_3575136("3575136 (Bulkload)"),
    P_1100149("1100149 (Initial)"),
    P_1684443_OUTRIGGER_CAM("1684443_OUTRIGGER_CAM (Initial)"),
    P_3574715("3574715 (Initial)"),
    P_3574688("3574688 (Initial)");

    private final String partName;

    JasperCirApiPartsEnum(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return this.partName;
    }
}
