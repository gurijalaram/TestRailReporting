package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "LicensesSchema.json")
public class Licenses extends Pagination {
    private List<LicenseResponse> items;
    private Licenses response;

    public Licenses getResponse() {
        return response;
    }

    public List<LicenseResponse> getItems() {
        return items;
    }
}
