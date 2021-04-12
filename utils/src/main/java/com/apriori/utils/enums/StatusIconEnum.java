package com.apriori.utils.enums;

public enum StatusIconEnum {

    CUBE("cube"),
    CAD("link"),
    LOCK("lock"),
    UNLOCK("unlock"),
    USER("user"),
    VERIFIED("shield-check");

    private final String statusIcon;

    StatusIconEnum(String statusIcon) {
        this.statusIcon = statusIcon;
    }

    public String getStatusIcon() {
        return this.statusIcon;
    }
}
