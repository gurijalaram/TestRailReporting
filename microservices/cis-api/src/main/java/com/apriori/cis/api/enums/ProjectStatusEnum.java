package com.apriori.cis.api.enums;

public enum ProjectStatusEnum {
    OPEN("OPEN"),
    COMPLETED("COMPLETED"),
    IN_NEGOTIATION("IN_NEGOTIATION"),
    INVALID("INVALID");

    private final String projectStatus;

    ProjectStatusEnum(String discussionStatus) {
        this.projectStatus = discussionStatus;
    }

    public String getProjectStatus() {
        return projectStatus;
    }
}