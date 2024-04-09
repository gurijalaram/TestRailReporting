package com.apriori.cir.api.enums;

public enum ComponentCostICModifiedEnum {

    EXPORT_SET_NAME_ID("exportSetName"),
    COMPONENT_TYPE_ID("componentType"),
    LATEST_EXPORT_DATE_ID("latestExportDate"),
    CREATED_BY_ID("createdBy"),
    LAST_MODIFIED_BY_ID("lastModifiedBy"),
    COMPONENT_NUMBER_ID("componentNumber"),
    SCENARIO_NAME_ID("scenarioName"),
    COMPONENT_SELECT_ID("componentSelect"),
    COMPONENT_COST_CURRENCY_CODE_ID("componentCostCurrencyCode"),
    EXPORT_SET_NAME_VALUE("~NOTHING~"),
    COMPONENT_TYPE_VALUE("~NOTHING~"),
    LATEST_EXPORT_DATE_VALUE(""),
    CREATED_BY_VALUE("~NOTHING~"),
    LAST_MODIFIED_BY_VALUE("~NOTHING~"),
    COMPONENT_NUMBER_VALUE("%"),
    SCENARIO_NAME_VALUE("~NOTHING~"),
    COMPONENT_SELECT_VALUE("1"),
    COMPONENT_COST_CURRENCY_CODE_VALUE("USD");

    private final String nameOrValueItem;

    ComponentCostICModifiedEnum(String nameOrValueItem) {
        this.nameOrValueItem = nameOrValueItem;
    }

    public String getNameOrValueItem() {
        return this.nameOrValueItem;
    }
}
