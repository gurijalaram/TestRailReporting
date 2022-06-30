package com.apriori.cds.objects.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SubLicenseAssociationSchema.json")
public class SubLicenseAssociation extends Pagination {
    private List<SubLicenseAssociationItems> items;
    private SubLicenseAssociation response;

    public SubLicenseAssociation getResponse() {
        return this.response;
    }

    public List<SubLicenseAssociationItems> getItems() {
        return this.items;
    }
}
