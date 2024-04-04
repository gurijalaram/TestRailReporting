package com.apriori.cir.api.models.enums;

public enum InputControlsEnum {
    APRIORI_COST_MAX("aPrioriCostMax"),
    APRIORI_COST_MIN("aPrioriCostMin"),
    APRIORI_MASS_MAX("aPrioriMassMax"),
    APRIORI_MASS_MIN("aPrioriMassMin"),
    ASSEMBLY_SELECT("assemblySelect"),
    COMPONENT_COST_CURRENCY("componentCostCurrencyCode"),
    COMPONENT_COST_MAX("componentCostMax"),
    COMPONENT_COST_MIN("componentCostMin"),
    COMPONENT_SELECT("componentSelect"),
    COMPONENT_TYPE("componentType"),
    COST_METRIC("costMetric"),
    CURRENCY("currencyCode"),
    DTC_SCORE("dtcScore"),
    EARLIEST_EXPORT_DATE("earliestExportDate"),
    END_DATE("endDate"),
    EXPORT_DATE("exportDate"),
    EXPORT_SET_NAME("exportSetName"),
    LATEST_COST_DATE("latestCostDate"),
    LATEST_EXPORT_DATE("latestExportDate"),
    MASS_METRIC("massMetric"),
    MINIMUM_ANNUAL_SPEND("annualSpendMin"),
    PERCENT_DIFFERENCE_THRESHOLD("percentDifferenceThreshold"),
    PROCESS_GROUP("processGroup"),
    PROJECT_ROLLUP("projectRollup"),
    SCENARIO_NAME("scenarioName"),
    SORT_ORDER("sortOrder"),
    START_DATE("startDate"),
    ROLLUP("rollup"),
    TRENDING_PERIOD("trendingPeriod");

    private final String inputControlId;

    InputControlsEnum(String inputControlId) {
        this.inputControlId = inputControlId;
    }

    public String getInputControlId() {
        return this.inputControlId;
    }
}
