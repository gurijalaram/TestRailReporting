package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/ApplicationsSchema.json")
public class Applications extends Pagination {
    @JsonProperty
    private List<Application> items;
    @JsonProperty
    private Applications response;

    public Applications getResponse() {
        return this.response;
    }

    public Applications setResponse(Applications response) {
        this.response = response;
        return this;
    }

    public Applications setItems(List<Application> items) {
        this.items = items;
        return this;
    }

    public List<Application> getItems() {
        return this.items;
    }
}
