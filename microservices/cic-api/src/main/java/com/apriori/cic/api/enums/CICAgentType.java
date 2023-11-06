package com.apriori.cic.api.enums;

public enum CICAgentType {
    WINDCHILL("windchill"),
    TEAM_CENTER("teamcenter"),
    FILE_SYSTEM("file_system");

    private final String agentType;

    CICAgentType(String type) {
        agentType = type;
    }

    public String getAgentType() {
        return agentType;
    }
}
