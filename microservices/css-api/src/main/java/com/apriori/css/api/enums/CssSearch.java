package com.apriori.css.api.enums;

public enum CssSearch {
    COMPONENT_NAME_EQ("componentName[EQ],"),
    COMPONENT_NAME_CN("componentName[CN],"),
    COMPONENT_IDENTITY_EQ("componentIdentity[EQ],"),
    COMPONENT_TYPE_EQ("componentType[EQ],"),
    SCENARIO_IDENTITY_EQ("scenarioIdentity[EQ],"),
    SCENARIO_NAME_EQ("scenarioName[EQ],"),
    SCENARIO_NAME_CN("scenarioName[CN],"),
    SCENARIO_STATE_EQ("scenarioState[EQ],"),
    LAST_ACTION_EQ("lastAction[EQ],"),
    LATEST_EQ("latest[EQ],"),
    ITERATION_EQ("iteration[EQ],"),
    SCENARIO_LOCKED_EQ("scenarioLocked[EQ],"),
    SCENARIO_OWNED_BY_EQ("scenarioOwnedBy[EQ],"),
    SCENARIO_PUBLISHED_EQ("scenarioPublished[EQ],"),
    SCENARIO_CREATED_AT_GT("scenarioCreatedAt[GT],"),
    PAGE_SIZE("pageSize,");

    private final String key;

    CssSearch(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
