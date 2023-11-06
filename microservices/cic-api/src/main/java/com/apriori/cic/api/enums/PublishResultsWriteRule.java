package com.apriori.cic.api.enums;

public enum PublishResultsWriteRule {
    CONSTANT("CONSTANT"),
    WORKFLOW_GENERATED_VALUE("WORKFLOW_GENERATED_VALUE");

    private final String writingRule;

    PublishResultsWriteRule(String wRule) {
        writingRule = wRule;
    }

    public String getWritingRule() {
        return writingRule;
    }
}
