package com.apriori.cds.api.enums;

import lombok.Getter;

@Getter
public enum ApplicationEnum {
    AP_PRO("ap-pro"),
    CIA("cia"),
    CIR("cir"),
    ACS("acs");

    private final String application;

    ApplicationEnum(String application) {
        this.application = application;
    }
}
