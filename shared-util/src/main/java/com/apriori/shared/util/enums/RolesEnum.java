package com.apriori.shared.util.enums;

import lombok.Getter;

@Getter
public enum RolesEnum {

    APRIORI_DEVELOPER("APRIORI_DEVELOPER"), //equal to old access level 'admin'
    APRIORI_EXPERT("APRIORI_EXPERT"),
    APRIORI_CONTRIBUTOR("APRIORI_CONTRIBUTOR"),
    APRIORI_ANALYST("APRIORI_ANALYST"),
    APRIORI_DESIGNER("APRIORI_DESIGNER"), //equal to old access level 'common'
    APRIORI_EDC("APRIORI_EDC");

    private final String role;

    RolesEnum(String role) {
        this.role = role;
    }
}
