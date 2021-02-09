package com.apriori.apibase.services.cas.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cas/CasCustomersSchema.json")
public class Customers extends Pagination {
    @JsonProperty
    private List<Customer> items;
    @JsonProperty
    private Customers response;

    public Customers getResponse() {
        return this.response;
    }

    public Customers setResponse(Customers response) {
        this.response = response;
        return this;
    }

    public Customers setItems(List<Customer> items) {
        this.items = items;
        return this;
    }

    public List<Customer> getItems() {
        return this.items;
    }
}
