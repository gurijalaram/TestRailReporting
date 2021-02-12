package com.apriori.cds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "common/ApplicationsSchema.json")
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
