package com.apriori.cir.api.enums;

public enum ScenarioComparisonICModifiedEnum {

    USE_LATEST_EXPORT_ID("useLatestExport"),
    EARLIEST_EXPORT_DATE_ID("earliestExportDate"),
    LATEST_EXPORT_DATE_ID("earliestExportDate"),
    EXPORT_SET_NAME_ID("exportSetName"),
    ALL_EXPORT_IDS_ID("allExportIDs"),
    COMPONENT_TYPE_ID("componentType"),
    CREATED_BY_ID("createdBy"),
    LAST_MODIFIED_BY_ID("lastModifiedBy"),
    PART_NUMBER_ID("partNumber"),
    SCENARIO_NAME_ID("scenarioName"),
    SCENARIO_TO_COMPARE_IDS_ID("scenarioToCompareIDs"),
    SCENARIO_IDS_ID("scenarioIDs"),
    CURRENCY_CODE_ID("currencyCode"),
    USE_LATEST_EXPORT_VALUE("Scenario"),
    EARLIEST_EXPORT_DATE_VALUE("2015-12-06 06:24:08"),
    LATEST_EXPORT_DATE_VALUE("2024-04-04 07:24:08"),
    EXPORT_SET_NAME_VALUE("~NOTHING~"),
    ALL_EXPORT_IDS_VALUE("~NOTHING~"),
    COMPONENT_TYPE_VALUE("~NOTHING~"),
    CREATED_BY_VALUE("~NOTHING~"),
    LAST_MODIFIED_BY_VALUE("~NOTHING~"),
    PART_NUMBER_VALUE("%"),
    SCENARIO_NAME_VALUE("~NOTHING~"),
    SCENARIO_TO_COMPARE_IDS_VALUE("187"),
    SCENARIO_IDS_VALUE("187"),
    CURRENCY_CODE("USD");

    private final String nameOrValueItem;

    ScenarioComparisonICModifiedEnum(String nameOrValueItem) {
        this.nameOrValueItem = nameOrValueItem;
    }

    public String getNameOrValueItem() {
        return this.nameOrValueItem;
    }
}
