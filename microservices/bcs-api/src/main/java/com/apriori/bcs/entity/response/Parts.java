package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "PartsSchema.json")
public class Parts extends Pagination {
    private Parts response;
    private List<Part> items;

    public Parts getResponse() {
        return this.response;
    }

    public Parts setResponse(Parts response) {
        this.response = response;
        return this;
    }

    public List<Part> getItems() {
        return this.items;
    }

    public Parts setItems(List<Part> items) {
        this.items = items;
        return this;
    }
}
