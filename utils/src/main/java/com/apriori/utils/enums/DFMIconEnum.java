package com.apriori.utils.enums;

public enum DFMIconEnum {

    CRITICAL("var(--danger-light)"),
    HIGH("var(--warning-light)"),
    MEDIUM("var(--info-light)"),
    LOW("var(--success-light)");

    private final String icon;

    DFMIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
