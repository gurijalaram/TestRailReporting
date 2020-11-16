package com.apriori.utils.enums.reports;

public enum DtcScoreEnum {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    ALL("High, Medium, Low");

    private final String dtcScoreName;

    DtcScoreEnum(String dtcScoreName) {
        this.dtcScoreName = dtcScoreName;
    }

    /**
     * Gets export set name
     *
     * @return String
     */
    public String getDtcScoreName() {
        return this.dtcScoreName;
    }
}