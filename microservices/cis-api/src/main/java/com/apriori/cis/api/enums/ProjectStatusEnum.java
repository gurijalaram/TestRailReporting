package com.apriori.cis.api.enums;

public enum ProjectStatusEnum {
    OPEN("OPEN"),
    COMPLETED("COMPLETED"),
    IN_NEGOTIATION("IN_NEGOTIATION"),
    INVALID("INVALID");

    private final String projectStatus;

    ProjectStatusEnum(String dStatus) {
        this.projectStatus = dStatus;
    }

    public String getProjectStatus() {
        return projectStatus;
    }
}