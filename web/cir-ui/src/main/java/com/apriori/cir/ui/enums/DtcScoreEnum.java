package com.apriori.cir.ui.enums;

public enum DtcScoreEnum {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    ALL("High, Low, Medium");

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