package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/ApplicationsSchema.json")
public class Applications extends Pagination {
    private List<Application> items;
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
