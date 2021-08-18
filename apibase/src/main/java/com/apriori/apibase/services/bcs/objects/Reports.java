package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "CisReportsSchema.json")
public class Reports extends Pagination {
    private Reports response;
    private List<Report> items;

    public Reports getResponse() {
        return this.response;
    }

    public Reports setResponse(Reports response) {
        this.response = response;
        return this;
    }

    public List<Report> getItems() {
        return this.items;
    }

    public Reports setItems(List<Report> items) {
        this.items = items;
        return this;
    }
}
