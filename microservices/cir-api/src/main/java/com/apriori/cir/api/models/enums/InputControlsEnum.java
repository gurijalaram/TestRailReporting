package com.apriori.cir.api.models.enums;

public enum InputControlsEnum {
    APRIORI_COST_MAX("aPrioriCostMax"),
    APRIORI_COST_MIN("aPrioriCostMin"),
    APRIORI_MASS_MAX("aPrioriMassMax"),
    APRIORI_MASS_MIN("aPrioriMassMin"),
    ASSEMBLY_SELECT("assemblySelect"),

    CURRENCY("currencyCode"),
    COMPONENT_COST_CURRENCY("componentCostCurrencyCode"),
    COMPONENT_COST_MIN("componentCostMin"),
    COMPONENT_COST_MAX("componentCostMax"),
    COMPONENT_SELECT("componentSelect"),
    COST_METRIC("costMetric"),
    END_DATE("endDate"),
    EXPORT_SET_NAME("exportSetName"),
    LATEST_COST_DATE("latestCostDate"),
    MASS_METRIC("massMetric"),
    PROCESS_GROUP("processGroup"),
    DTC_SCORE("dtcScore"),
    MINIMUM_ANNUAL_SPEND("annualSpendMin"),
    PERCENT_DIFFERENCE_THRESHOLD("percentDifferenceThreshold"),
    SORT_ORDER("sortOrder"),
    START_DATE("startDate"),
    TRENDING_PERIOD("trendingPeriod"),
    PROJECT_ROLLUP("projectRollup"),
    LATEST_EXPORT_DATE("latestExportDate"),
    EARLIEST_EXPORT_DATE("earliestExportDate"),
    EXPORT_DATE("exportDate"),
    ROLLUP("rollup");

    private final String inputControlId;

    InputControlsEnum(String inputControlId) {
        this.inputControlId = inputControlId;
    }

    public String getInputControlId() {
        return this.inputControlId;
    }
}
