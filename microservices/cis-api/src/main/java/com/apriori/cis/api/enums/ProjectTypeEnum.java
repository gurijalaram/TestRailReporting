package com.apriori.cis.api.enums;

public enum ProjectTypeEnum {
    INTERNAL("INTERNAL"),
    INVALID("INVALID");

    private final String projectType;

    ProjectTypeEnum(String discussionStatus) {
        this.projectType = discussionStatus;
    }

    public String getProjectType() {
        return projectType;
    }
}