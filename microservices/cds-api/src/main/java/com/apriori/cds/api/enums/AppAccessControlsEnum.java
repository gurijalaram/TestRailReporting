package com.apriori.cds.api.enums;

public enum AppAccessControlsEnum {

    CUS("Current User Service"),
    CSS("Cloud Search Service"),
    ACH("aPriori Cloud Home"),
    AA("aP Analytics"),
    AD("aP Design"),
    AP("aP Pro"),
    ADMIN("aP Admin"),
    EDC("Electronics Data Collection"),
    FMS("File Management Service"),
    ACS("Anonymized Costing Service"),
    AW("aP Workspace"),
    AC("aP Connect"),
    CA("Customer Admin");

    private final String app;

    AppAccessControlsEnum(String app) {
        this.app = app;
    }

    public String getApp() {
        return app;
    }

    public static AppAccessControlsEnum fromString(String value) {
        for (AppAccessControlsEnum b : AppAccessControlsEnum.values()) {
            if (b.app.equalsIgnoreCase(value)) {
                return b;
            }
        }
        return null;
    }
}
