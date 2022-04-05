package com.utils;

import java.util.EnumSet;

public enum UploadStatusEnum {

    // TODO: 05/04/2022 cn - need to find locator for this
    FAILED("Failed"),
    UPLOADED("succeeded"),
    UPLOADING("submitting"),
    // TODO: 05/04/2022 cn - need to find locator for this
    PENDING("Pending");

    private final String uploadStatus;

    UploadStatusEnum(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public static EnumSet<UploadStatusEnum> completedGroup = EnumSet.of(FAILED,UPLOADED);
    public static EnumSet<UploadStatusEnum> waitingGroup = EnumSet.of(UPLOADING,PENDING);
}
