package com.apriori.cds.objects.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/SubLicenseAssociationUserSchema.json")
public class SubLicenseAssociation extends Pagination {
    private List<SubLicenseAssociationUser> items;
    private SubLicenseAssociation response;

    public SubLicenseAssociation getResponse() {
        return this.response;
    }

    public List<SubLicenseAssociationUser> getItems() {
        return this.items;
    }
    }
