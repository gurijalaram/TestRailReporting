package com.apriori.cir.models.enums;

public enum InputControlsEnum {

    CURRENCY("currencyCode"),
    COMPONENT_COST_CURRENCY("componentCostCurrencyCode"),
    COST_METRIC("costMetric"),
    END_DATE("endDate"),
    EXPORT_SET_NAME("exportSetName"),
    MASS_METRIC("massMetric"),
    PROCESS_GROUP("processGroup"),
    DTC_SCORE("dtcScore"),
    MINIMUM_ANNUAL_SPEND("annualSpendMin"),
    SORT_ORDER("sortOrder"),
    START_DATE("startDate"),
    TRENDING_PERIOD("trendingPeriod"),
    PROJECT_ROLLUP("projectRollup");

    private final String inputControlId;

    InputControlsEnum(String inputControlId) {
        this.inputControlId = inputControlId;
    }

    public String getInputControlId() {
        return this.inputControlId;
    }
}
