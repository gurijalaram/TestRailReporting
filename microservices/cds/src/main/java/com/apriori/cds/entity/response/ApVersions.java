package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "common/ApVersionsSchema.json")
public class ApVersions extends Pagination {
    @JsonProperty
    private List<ApVersion> items;
    @JsonProperty
    private ApVersions response;

    public ApVersions getResponse() {
        return this.response;
    }

    public ApVersions setResponse(ApVersions response) {
        this.response = response;
        return this;
    }

    public ApVersions setItems(List<ApVersion> items) {
        this.items = items;
        return this;
    }

    public List<ApVersion> getItems() {
        return this.items;
    }
}
