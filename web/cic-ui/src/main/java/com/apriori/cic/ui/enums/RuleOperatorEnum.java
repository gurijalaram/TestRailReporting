package com.apriori.cic.ui.enums;

/*
Enum for Rule Operator in GUI during workflow creation in Query Definition section
 */
public enum RuleOperatorEnum {
    EQUAL("equal"),
    NOT_EQUAL("not equal"),
    CONTAINS("contains"),
    BEGINS_WITH("begins with"),
    ENDS_WITH("ends with");

    private final String ruleOperator;

    RuleOperatorEnum(String mRule) {
        ruleOperator = mRule;
    }

    public String getRuleOperator() {
        return ruleOperator;
    }
}
