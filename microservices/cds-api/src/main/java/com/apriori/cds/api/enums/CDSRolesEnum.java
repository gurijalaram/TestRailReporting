package com.apriori.cds.api.enums;

public enum CDSRolesEnum {
    CONTRIBUTOR("APRIORI_CONTRIBUTOR"),
    ANALYST("APRIORI_ANALYST"),
    DESIGNER("APRIORI_DESIGNER"),
    DEVELOPER("APRIORI_DEVELOPER"),
    EXPERT("APRIORI_EXPERT"),
    ANALYST_CONNECT("APRIORI_ANALYST + CONNECT ADMIN"),
    DESIGNER_CONNECT_USER("APRIORI_DESIGNER + CONNECT ADMIN + USER ADMIN"),
    EXPERT_CONNECT_USER("APRIORI_EXPERT + CONNECT ADMIN + USER ADMIN"),
    EXPERT_CONNECT_USER_SANDBOX("APRIORI_EXPERT + CONNECT ADMIN + USER ADMIN + SANDBOX");

    private final String role;

    CDSRolesEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
