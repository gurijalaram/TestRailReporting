package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "SubLicensesSchema.json")
public class SubLicenses extends Pagination {
    private List<SubLicense> items;
    private SubLicenses response;

    public SubLicenses getResponse() {
        return this.response;
    }

    public SubLicenses setResponse(SubLicenses response) {
        this.response = response;
        return this;
    }

    public List<SubLicense> getItems() {
        return this.items;
    }

    public SubLicenses setItems(List<SubLicense> items) {
        this.items = items;
        return this;
    }
}
