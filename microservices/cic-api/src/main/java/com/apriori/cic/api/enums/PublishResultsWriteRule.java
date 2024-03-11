package com.apriori.cic.api.enums;

public enum PublishResultsWriteRule {
    CONSTANT("CONSTANT", "Constant"),
    WORKFLOW_GENERATED_VALUE("WORKFLOW_GENERATED_VALUE", "Workflow Generated Value");

    private final String writingRule;
    private final String uiWritingRule;

    PublishResultsWriteRule(String wRule, String uiWriteRule) {
        writingRule = wRule;
        uiWritingRule = uiWriteRule;
    }

    public String getWritingRule() {
        return writingRule;
    }

    public String getGuiWritingRule() {
        return uiWritingRule;
    }
}
