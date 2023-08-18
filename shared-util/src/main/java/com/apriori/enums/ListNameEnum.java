package com.apriori.enums;

public enum ListNameEnum {
    EXPORT_SET("set selection."),
    CREATED_BY("Created By"),
    LAST_MODIFIED_BY("Last Modified By"),
    PARTS("Select Parts "),
    PARTS_NO_SPACE("Select Parts"),
    COMPONENT_TYPE("Scenario Type"),
    SCENARIO_NAME("Scenario Name"),
    SCENARIOS_TO_COMPARE("Scenarios to Compare");

    private final String listName;

    ListNameEnum(String listName) {
        this.listName = listName;
    }

    /**
     * Gets List name
     *
     * @return String
     */
    public String getListName() {
        return this.listName;
    }
}