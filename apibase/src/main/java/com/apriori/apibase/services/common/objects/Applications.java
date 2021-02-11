package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "common/ApplicationsSchema.json")
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

    /*public Applications setItems(List<Application> items) {
        this.items = items;
        return this;
    }

    public List<Application> getItems() {
        return this.items;
    }*/
}
