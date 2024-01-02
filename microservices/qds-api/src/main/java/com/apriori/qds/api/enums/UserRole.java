package com.apriori.qds.api.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    DEFAULT("DEFAULT"),
    GUEST("GUEST");

    private final String userRole;

    UserRole(String dStatus) {
        this.userRole = dStatus;
    }

    public String getUserRole() {
        return userRole;
    }
}