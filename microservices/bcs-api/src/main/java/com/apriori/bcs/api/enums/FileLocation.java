package com.apriori.bcs.api.enums;

public enum FileLocation {
    LOCAL("LOCAL"),
    CLOUD("CLOUD");

    private final String fileLocation;

    FileLocation(String st) {
        fileLocation = st;
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
