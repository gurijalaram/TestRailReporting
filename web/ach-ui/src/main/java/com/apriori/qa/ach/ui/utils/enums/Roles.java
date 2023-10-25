package com.apriori.qa.ach.ui.utils.enums;

public enum Roles {

    APRIORI_ANALYST("APRIORI ANALYST"),
    APRIORI_CONTRIBUTOR("APRIORI CONTRIBUTOR"),
    APRIORI_DESIGNER("APRIORI DESIGNER"),
    APRIORI_DEVELOPER("APRIORI DEVELOPER"),
    APRIORI_EDC("APRIORI EDC"),
    APRIORI_EXPERT("APRIORI EXPERT");

    private final String role;

    Roles(String name) {
        this.role = name;
    }

    /**
     * Gets role name from Enum
     *
     * @return String
     */
    public String getRole() {
        return this.role;
    }
}
