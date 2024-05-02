package com.apriori.cds.api.enums;

import lombok.Getter;

@Getter
public enum ApplicationEnum {
    AP_PRO("ap-pro"),
    CIA("ci-admin"),
    CIR("ci-report"),
    ACS("acs"),
    CAS("cas"),
    ECD("edc"),
    CIS("cis"),
    CID("ci-design"),
    CIC("ci-connect"),
    CLOUD_HOME("cloud");

    private final String application;

    ApplicationEnum(String application) {
        this.application = application;
    }
}
