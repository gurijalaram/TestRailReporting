package com.apriori.cirapi.entity.enums;

public enum InputControlsEnum {

    CURRENCY("currencyCode"),
    COMPONENT_COST_CURRENCY("componentCostCurrencyCode"),
    COST_METRIC("costMetric"),
    MASS_METRIC("massMetric"),
    PROCESS_GROUP("processGroup"),
    DTC_SCORE("dtcScore"),
    MINIMUM_ANNUAL_SPEND("annualSpendMin"),
    SORT_ORDER("sortOrder"),
    PROJECT_ROLLUP("projectRollup");

    private final String inputControlId;

    InputControlsEnum(String inputControlId) {
        this.inputControlId = inputControlId;
    }

    public String getInputControlId() {
        return this.inputControlId;
    }
}
