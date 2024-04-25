package com.apriori.qds.api.enums;

public enum DiscussionStatus {
    ACTIVE("ACTIVE"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED"),
    RESOLVED("RESOLVED");

    private final String discussionStatus;

    DiscussionStatus(String discussionStatus) {
        this.discussionStatus = discussionStatus;
    }

    public String getDiscussionStatus() {
        return discussionStatus;
    }
}