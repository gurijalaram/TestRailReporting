package com.apriori.css.entity.enums;

public enum CssSearch {

    SCENARIO_NAME_EQ("scenarioName[EQ],"),
    SCENARIO_NAME_CN("scenarioName[CN],"),
    SCENARIO_STATE_EQ("scenarioState[EQ],"),
    COMPONENT_NAME_EQ("componentName[EQ],"),
    COMPONENT_NAME_CN("componentName[CN],"),
    LAST_ACTION_EQ("lastAction[EQ],");

    private final String operand;

    CssSearch(String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return this.operand;
    }
}
