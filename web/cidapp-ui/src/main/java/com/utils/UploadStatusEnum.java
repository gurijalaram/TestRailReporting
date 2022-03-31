package com.utils;

public enum UploadStatusEnum {

    FAILED("Failed"),
    UPLOADING("Uploading"),
    UPLOADED("Uploaded"),
    PENDING("Pending");

    private final String uploadStatus;

    UploadStatusEnum(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }
}
