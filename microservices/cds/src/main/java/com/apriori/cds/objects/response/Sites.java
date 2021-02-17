package com.apriori.cds.objects.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/SitesSchema.json")
public class Sites extends Pagination {
    private List<Site> items;
    private Sites response;

    public Sites getResponse() {
        return this.response;
    }

    public Sites setResponse(Sites response) {
        this.response = response;
        return this;
    }

    public Sites setItems(List<Site> items) {
        this.items = items;
        return this;
    }

    public List<Site> getItems() {
        return this.items;
    }
}

