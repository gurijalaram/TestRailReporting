package com.apriori.entity.response;

import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "LicensesSchema.json")
public class Licenses {
    private List<com.apriori.cds.entity.response.LicenseResponse> items;
    private com.apriori.cds.objects.response.Licenses response;

    public com.apriori.cds.objects.response.Licenses getResponse() {
        return response;
    }

    public List<LicenseResponse> getItems() {
        return items;
    }
}
