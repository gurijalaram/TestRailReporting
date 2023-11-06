package com.apriori.cid.ui.utils;

public enum AssemDfmIconEnum {

    CRITICAL("var(--danger-light)"),
    HIGH("var(--warning-light)"),
    MEDIUM("var(--info-light)"),
    LOW("var(--success-light)");

    private final String icon;

    AssemDfmIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
