package com.apriori.cic.api.enums;

public enum CICEndpointType {
    CICAPI("CICAPI"),
    CICUIAPI("CICUIAPI");

    private final String endpointType;

    CICEndpointType(String st) {
        endpointType = st;
    }

    public String getEndpointType() {
        return endpointType;
    }
}