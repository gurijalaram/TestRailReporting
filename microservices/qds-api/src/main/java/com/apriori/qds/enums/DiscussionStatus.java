package com.apriori.qds.enums;

public enum DiscussionStatus {
    ACTIVE("ACTIVE"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED"),
    RESOLVED("RESOLVED");

    private final String discussionStatus;

    DiscussionStatus(String dStatus) {
        this.discussionStatus = dStatus;
    }

    public String getDiscussionStatus() {
        return discussionStatus;
    }
}