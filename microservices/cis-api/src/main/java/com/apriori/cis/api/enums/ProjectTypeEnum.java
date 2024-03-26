package com.apriori.cis.api.enums;

public enum ProjectTypeEnum {
    INTERNAL("INTERNAL"),
    INVALID("INVALID");

    private final String projectType;

    ProjectTypeEnum(String dStatus) {
        this.projectType = dStatus;
    }

    public String getProjectType() {
        return projectType;
    }
}