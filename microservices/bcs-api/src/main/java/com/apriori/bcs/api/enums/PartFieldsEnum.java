package com.apriori.bcs.api.enums;

public enum PartFieldsEnum {
    SCENARIO_NAME("ScenarioName"),
    EXTERNAL_ID("ExternalId"),
    DESCRIPTION("Description"),
    FILE_NAME("filename"),
    ANNUAL_VOLUME("AnnualVolume"),
    BATCH_SIZE("BatchSize"),
    PROCESS_GROUP("ProcessGroup"),
    PRODUCTION_LIFE("ProductionLife"),
    VPE_NAME("VpeName"),
    MATERIAL_NAME("MaterialName"),
    GENERATE_WATCHPOINT_REPORT("generateWatchpointReport"),
    UDAS("udas"),
    TARGET_COST("targetCost"),
    TARGET_MASS("targetMass"),
    MACHINING_MODE("machiningMode"),
    DATA("data");

    private final String partFieldName;

    PartFieldsEnum(String st) {
        partFieldName = st;
    }

    public String getPartFieldName() {
        return partFieldName;
    }
}
