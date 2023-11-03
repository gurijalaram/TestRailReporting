package com.apriori.cic.api.enums;

public enum CICAgentStatus {
    QUERY_IN_PROGRESS("Query In Progress"),
    FINISHED("Finished"),
    PENDING("Pending"),
    CANCELLED("Cancelled"),
    COSTING("Costing"),
    PLM_WRITE_ACTION("PLM Write Action In Progress");

    private final String agentStatus;

    CICAgentStatus(String status) {
        agentStatus = status;
    }

    public String getAgentStatus() {
        return agentStatus;
    }
}
