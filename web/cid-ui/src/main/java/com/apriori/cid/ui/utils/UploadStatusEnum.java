package com.apriori.cid.ui.utils;

import java.util.EnumSet;

public enum UploadStatusEnum {

    FAILED("failed"),
    UPLOADED("success"),
    UPLOADING("submitting"),
    PENDING("pending");

    private final String uploadStatus;

    UploadStatusEnum(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public static EnumSet<UploadStatusEnum> completedGroup = EnumSet.of(UPLOADED, FAILED);
    public static EnumSet<UploadStatusEnum> waitingGroup = EnumSet.of(UPLOADING, PENDING);
}
