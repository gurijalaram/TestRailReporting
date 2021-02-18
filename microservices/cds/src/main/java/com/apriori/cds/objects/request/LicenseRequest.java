package com.apriori.cds.objects.request;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/LicenseRequestSchema.json")
public class LicenseRequest {
    private License license;

    public License getLicense() {
        return license;
    }

    public LicenseRequest setLicense(License license) {
        this.license = license;
        return this;
    }
}
