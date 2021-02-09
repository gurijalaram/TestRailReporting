package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "cas/CasCustomersSchema.json")
public class Customers {
    @JsonProperty
    private CdsPagination response;
    @JsonProperty("response")
    private Customer customerResponse;

    public CdsPagination getResponse() {
        return this.response;
    }

    public Customers setResponse(CdsPagination response) {
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
