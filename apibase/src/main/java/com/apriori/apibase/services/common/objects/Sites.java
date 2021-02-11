package com.apriori.apibase.services.common.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cds/SitesSchema.json")
public class Sites extends Pagination {
    @JsonProperty
    private List<Site> items;
    @JsonProperty
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

