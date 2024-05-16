package com.apriori.shared.util.enums;

public enum RolesEnum {

    APRIORI_DEVELOPER("APRIORI_DEVELOPER"),
    APRIORI_EXPERT("APRIORI_EXPERT"),
    APRIORI_CONTRIBUTOR("APRIORI_CONTRIBUTOR"),
    APRIORI_ANALYST("APRIORI_ANALYST"),
    APRIORI_DESIGNER("APRIORI_DESIGNER"),
    APRIORI_EDC("APRIORI_EDC");

    private final String role;

    RolesEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
