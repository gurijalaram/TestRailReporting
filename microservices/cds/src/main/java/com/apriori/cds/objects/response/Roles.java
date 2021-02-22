package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/RolesSchema.json")
public class Roles extends Pagination {
    private List<Role> items;
    private Roles response;

    public Roles getResponse() {
        return this.response;
    }

    public List<Role> getItems() {
        return this.items;
    }
}