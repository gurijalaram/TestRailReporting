package com.apriori.cds.api.enums;

import lombok.Getter;

@Getter
public enum RolesEnum {
    AP_ANALYST("AP_ANALYST"),
    AP_AUTOMATION("AP_AUTOMATION"),
    AP_CONNECT_USER("AP_CONNECT_USER"),
    AP_CONTRIBUTOR("AP_CONTRIBUTOR"),
    AP_DESIGNER("AP_DESIGNER"),
    AP_EDC("AP_EDC"),
    AP_EXPERT("AP_EXPERT"),
    AP_HIGH_MEM("AP_HIGH_MEM"),
    AP_PREVIEW("AP_PREVIEW"),
    AP_SANDBOX("AP_SANDBOX"),
    AP_USER("AP_USER"),
    AP_USER_ADMIN("AP_USER_ADMIN"),
    AP_EXPORT_ADMIN("AP_EXPORT_ADMIN");

    public final String roles;

    RolesEnum(String roles) {
        this.roles = roles;
    }
}
