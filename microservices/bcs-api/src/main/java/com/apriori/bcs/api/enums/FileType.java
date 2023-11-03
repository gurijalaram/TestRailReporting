package com.apriori.bcs.api.enums;

public enum FileType {
    TXT("TXT"),
    PDF("PDF");

    private final String fileType;

    FileType(String st) {
        fileType = st;
    }

    public String getFileType() {
        return fileType;
    }
}
