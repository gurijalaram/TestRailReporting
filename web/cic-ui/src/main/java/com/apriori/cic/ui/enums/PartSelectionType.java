package com.apriori.cic.ui.enums;

public enum PartSelectionType {
    QUERY("Query Definition"),
    REST("REST API Request");

    private final String partSelectionType;

    PartSelectionType(String st) {
        partSelectionType = st;
    }

    public String getPartSelectionType() {
        return partSelectionType;
    }
}
