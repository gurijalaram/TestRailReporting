package com.apriori.cir.api.models.enums;

public enum InputControlsEnum {
    APRIORI_COST_MAX("aPrioriCostMax"),
    APRIORI_COST_MIN("aPrioriCostMin"),
    APRIORI_MASS_MAX("aPrioriMassMax"),
    APRIORI_MASS_MIN("aPrioriMassMin"),
    ASSEMBLY_SELECT("assemblySelect"),
    ASSEMBLY_NUMBER_SEARCH_CRITERIA("assemblyNumber"),
    COMPONENT_COST_CURRENCY("componentCostCurrencyCode"),
    COMPONENT_COST_MAX("componentCostMax"),
    COMPONENT_COST_MIN("componentCostMin"),
    COMPONENT_SELECT("componentSelect"),
    COMPONENT_TYPE("componentType"),
    COMPONENT_NUMBER("componentNumber"),
    COST_METRIC("costMetric"),
    CURRENCY("currencyCode"),
    CREATED_BY("createdBy"),
    DTC_SCORE("dtcScore"),
    EARLIEST_EXPORT_DATE("earliestExportDate"),
    END_DATE("endDate"),
    EXPORT_DATE("exportDate"),
    EXPORT_SET_NAME("exportSetName"),
    LAST_MODIFIED_BY("lastModifiedBy"),
    LATEST_COST_DATE("latestCostDate"),
    LATEST_EXPORT_DATE("latestExportDate"),
    MASS_METRIC("massMetric"),
    MINIMUM_ANNUAL_SPEND("annualSpendMin"),
    PART_NUMBER_SEARCH_CRITERIA("partNumber"),
    PART_NUMBER("partNumber"),
    PERCENT_DIFFERENCE_THRESHOLD("percentDifferenceThreshold"),
    PROCESS_GROUP("processGroup"),
    PROJECT_ROLLUP("projectRollup"),
    SCENARIO_NAME("scenarioName"),
    SCENARIOS_TO_COMPARE_IDS("scenarioToCompareIDs"),
    SORT_ORDER("sortOrder"),
    START_DATE("startDate"),
    ROLLUP("rollup"),
    TRENDING_PERIOD("trendingPeriod"),
    USE_LATEST_EXPORT("useLatestExport");

    private final String inputControlId;

    InputControlsEnum(String inputControlId) {
        this.inputControlId = inputControlId;
    }

    public String getInputControlId() {
        return this.inputControlId;
    }
}
