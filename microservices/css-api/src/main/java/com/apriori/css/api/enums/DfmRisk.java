package com.apriori.css.api.enums;

public enum DfmRisk {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    private final String dfmRisk;

    DfmRisk(String dfmRisk) {
        this.dfmRisk = dfmRisk;
    }

    public String getDfmRisk() {
        return dfmRisk;
    }
}

