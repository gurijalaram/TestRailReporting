package com.apriori.cic.enums;

public enum MappingRule {
    MAPPED_FROM_PLM("Mapped from PLM"),
    CONSTANT("Constant"),
    DEFAULT_NO_PLM_VALUE("Default If No PLM Value");

    private final String mappingRule;

    MappingRule(String mRule) {
        mappingRule = mRule;
    }

    public String getMappingRule() {
        return mappingRule;
    }
}
