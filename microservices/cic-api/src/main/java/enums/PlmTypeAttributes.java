package enums;

/**
 * This enum is to supply data for costing output fields mapped with CIC UI (key) and PLM UI Attributes (value)
 */
public enum PlmTypeAttributes {

    PLM_MATERIAL_COST("materialCost", "MaterialCost"),
    PLM_FINISH_MASS("finishMass", "FinishMass"),
    PLM_CAPITAL_INVESTMENT("capitalInvestment", "CapitalInvestment"),
    PLM_LABOR_TIME("laborTime", "LaborTime"),
    PLM_ROUGH_MASS("roughMass", "RoughMass"),
    PLM_UTILIZATION("utilization", "Utilization"),
    PLM_FULLY_BURDENED_COST("fullyBurdenedCost", "ApFBC"),
    PLM_DFM_RISK("dfmRisk", "ApDFMRating"),
    PLM_CURRENCY_CODE("currencyCode", "CurrencyCode"),
    PLM_CYCLE_TIME("cycleTime", "ApCycleTime"),
    PLM_DFM_SCORE("dfmScore", "ApDFMRiskScore"),
    PLM_TOTAL_COST("totalCost", "ApPPC"),
    PLM_CUSTOM_STRING("UDA1", "String1"),
    PLM_CUSTOM_NUMBER("UDA2", "RealNumber1"),
    PLM_CUSTOM_DATE("UDA3", "DateTime1"),
    PLM_CUSTOM_MULTI("UDA4", "MultiselectString"),
    PLM_PROCESS_GROUP("processGroupName", "ApPG"),
    PLM_MATERIAL_NAME("materialName", "ApMaterial"),
    PLM_BATCH_SIZE("batchSize", "BatchSize"),
    PLM_ANNUAL_VOLUME("annualVolume", "AnnualVolume"),
    PLM_PRODUCTION_LIFE("productionLife", "ProductionLife"),
    PLM_DIGITAL_FACTORY("vpeName", "VPE");
    
    private final String key;
    private final String value;

    PlmTypeAttributes(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}

