package com.apriori.cic.ui.enums;

/*
Enum for Usage Rule in GUI during connector creation
 */
public enum UsageRule {
    READ_FROM("Read From"),
    WRITE_TO("Write To"),
    READ_AND_WRITE("Read and Write");

    private final String usageRule;

    UsageRule(String mRule) {
        usageRule = mRule;
    }

    public String getUsageRule() {
        return usageRule;
    }
}
