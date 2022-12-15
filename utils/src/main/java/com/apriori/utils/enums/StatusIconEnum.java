package com.apriori.utils.enums;

public enum StatusIconEnum {
    CUBE("cube"),
    CAD("link"),
    DISCONNECTED("link-slash"),
    LOCK("lock"),
    UNLOCK("lock-open"),
    USER("user"),
    VERIFIED("shield-check"),
    ASSEMBLY("assembly"),
    PUBLIC("users"),
    MISSING("missing"),
    VIRTUAL("virtual"),
    PRIVATE("user");

    private final String statusIcon;

    StatusIconEnum(String statusIcon) {
        this.statusIcon = statusIcon;
    }

    public String getStatusIcon() {
        return this.statusIcon;
    }
}
