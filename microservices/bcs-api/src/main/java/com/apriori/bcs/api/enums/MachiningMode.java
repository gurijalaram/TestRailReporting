package com.apriori.bcs.api.enums;

public enum MachiningMode {
    MAY_BE_MACHINED("MAY_BE_MACHINED"),
    NOT_MACHINED("NOT_MACHINED");

    private final String machiningMode;

    MachiningMode(String st) {
        machiningMode = st;
    }

    public String getMachiningMode() {
        return machiningMode;
    }
}
