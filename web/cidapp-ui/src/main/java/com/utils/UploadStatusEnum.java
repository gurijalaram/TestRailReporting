package com.utils;

public enum UploadStatusEnum {

    // TODO: 05/04/2022 cn - need to find locator for this
    FAILED("Failed"),
    UPLOADING("submitting"),
    UPLOADED("succeeded"),
    // TODO: 05/04/2022 cn - need to find locator for this
    PENDING("Pending");

    private final String uploadStatus;

    UploadStatusEnum(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }
}
