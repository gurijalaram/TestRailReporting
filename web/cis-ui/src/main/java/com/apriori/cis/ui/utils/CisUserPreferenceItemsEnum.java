package com.apriori.cis.ui.utils;

public enum CisUserPreferenceItemsEnum {

    DISPLAY_PREFERENCES("Display Preferences"),
    PROFILE("Profile");

    private final String items;

    CisUserPreferenceItemsEnum(String columns) {
        this.items = columns;
    }

    public String getItems() {
        return this.items;
    }
}
