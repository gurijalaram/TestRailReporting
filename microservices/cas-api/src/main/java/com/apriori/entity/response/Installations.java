package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/InstallationsSchema.json")
public class Installations extends Pagination {
    private List<Installation> items;
    private Installations response;

    public Installations getResponse() {
        return this.response;
    }

    public List<Installation> getItems() {
        return this.items;
    }
}
