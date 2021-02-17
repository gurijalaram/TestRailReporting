package com.apriori.apibase.services.cas;

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

    public Installations setResponse(Installations response) {
        this.response = response;
        return this;
    }

    public Installations setItems(List<Installation> items) {
        this.items = items;
        return this;
    }

    public List<Installation> getItems() {
        return this.items;
    }
}
