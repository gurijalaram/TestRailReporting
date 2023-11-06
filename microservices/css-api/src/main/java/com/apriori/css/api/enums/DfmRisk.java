package com.apriori.css.api.enums;

public enum DfmRisk {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    private final String dfmRisk;

    DfmRisk(String dRisk) {
        this.dfmRisk = dRisk;
    }

    public String getDfmRisk() {
        return dfmRisk;
    }
}

