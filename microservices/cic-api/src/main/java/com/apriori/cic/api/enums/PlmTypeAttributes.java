package com.apriori.cic.api.enums;

/**
 * This enum is to supply data for costing output fields mapped with CIC UI (key) and PLM UI Attributes (value)
 */
public enum PlmTypeAttributes {

    PLM_MATERIAL_COST("materialCost", "MaterialCost", "Material Cost"),
    PLM_FINISH_MASS("finishMass", "FinishMass", "Finish Mass"),
    PLM_CAPITAL_INVESTMENT("capitalInvestment", "CapitalInvestment", "Capital Investment"),
    PLM_LABOR_TIME("laborTime", "LaborTime", "Labor Time"),
    PLM_ROUGH_MASS("roughMass", "RoughMass", "Rough Mass"),
    PLM_UTILIZATION("utilization", "Utilization", "Utilization"),
    PLM_FULLY_BURDENED_COST("fullyBurdenedCost", "apFBC", "Fully Burdened Cost"),
    PLM_DFM_RISK("dfmRisk", "apDFMRating", "DFM Risk Rating"),
    PLM_CURRENCY_CODE("currencyCode", "USD", "Currency Code"),
    PLM_CYCLE_TIME("cycleTime", "apCycleTime", "Cycle Time"),
    PLM_DFM_SCORE("dfmScore", "apDFMRiskScore", "DFM Risk Score"),
    PLM_TOTAL_COST("totalCost", "apPPC", "Piece Part Cost"),
    PLM_CUSTOM_STRING("UDA1", "String1", "Custom String"),
    PLM_CUSTOM_NUMBER("UDA2", "RealNumber1", "Custom Number"),
    PLM_CUSTOM_DATE("UDA3", "DateTime1", "Custom Date"),
    PLM_CUSTOM_MULTI("UDA4", "MultiselectString", "Custom Multi"),
    PLM_CUSTOM_EMAIL("UDA5", "checkInUserEmail", "Check In User Email"),
    PLM_PROCESS_GROUP("processGroupName", "apPG", "Process Group"),
    PLM_MATERIAL_NAME("materialName", "apMaterial", "Material"),
    PLM_BATCH_SIZE("batchSize", "BatchSize", "Batch Size"),
    PLM_ANNUAL_VOLUME("annualVolume", "AnnualVolume", "Annual Volume"),
    PLM_PRODUCTION_LIFE("productionLife", "ProductionLife", "Production Life"),
    PLM_DIGITAL_FACTORY("vpeName", "VPE", "Digital Factory"),
    PLM_PART_ID("partId", "partId", "Part ID"),
    PLM_PART_NUMBER("partNumber", "Number", "Part Number"),
    PLM_REVISION("revisionNumber", "Revision", "Revision Number"),
    PLM_DESCRIPTION("description", "testDesc", "Description"),
    PLM_SCENARIO_NAME("scenarioName", "ScenarioName", "Scenario Name"),
    PLM_CAD_FILE_NAME("cadFileName", "cadFileName", "CAD File Name"),
    PLM_SEQUENCE_ID("scenarioName", "ScenarioName", "Sequence Id"),
    PLM_APRIORI_PART_NUMBER("aPrioriPartNumber", "aP Part Number", "aPriori Part Number"),
    PLM_COST_METRIC("costMetric", "Fully Burdened Cost", "Cost Metric"),
    PLM_MASS_METRIC("massMetric", "Finish Mass", "Mass Metric"),
    PLM_SORT_METRIC("sortMetric", "Part Number", "Sort Metric"),
    PLM_RISK_RATING("riskRating", "[\"All\"]", "Risk Rating"),
    PLM_COST_ROUNDING("roundToDollar", "true", "Cost Rounding"),
    PLM_MACHINING_MODE("machiningMode", "MAY_BE_MACHINED", "Machining Mode");
    
    private final String key;
    private final String value;
    private final String cicGuiField;

    PlmTypeAttributes(String key, String value, String cicGuiField) {
        this.key = key;
        this.value = value;
        this.cicGuiField = cicGuiField;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getCicGuiField() {
        return this.cicGuiField;
    }
}

