package com.apriori.qds.api.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    DEFAULT("DEFAULT"),
    GUEST("GUEST");

    private final String userRole;

    UserRole(String discussionStatus) {
        this.userRole = discussionStatus;
    }

    public String getUserRole() {
        return userRole;
    }
}