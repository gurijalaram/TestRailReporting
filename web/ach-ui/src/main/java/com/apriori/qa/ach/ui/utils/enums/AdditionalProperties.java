package com.apriori.qa.ach.ui.utils.enums;

public enum AdditionalProperties {
    AP_CONNECT_ADMIN("aP Connect Admin"),
    AP_USER_ADMIN("aP User Admin"),
    AP_HIGH_MEMORY("aP High Memory"),
    AP_EXPORT_ADMIN("aP Export Admin");

    private final String properties;

    AdditionalProperties(String name) {
        this.properties = name;
    }

    /**
     * Gets role name from Enum
     *
     * @return String
     */
    public String getProperties() {
        return this.properties;
    }
}

