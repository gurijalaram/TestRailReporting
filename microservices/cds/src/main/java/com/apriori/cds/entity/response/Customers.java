package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cds/CustomersSchema.json")
public class Customers extends Pagination {
    @JsonProperty
    private List<Customer> items;

    @JsonProperty
    private Customers response;

    public Customers getResponse() {
        return response;
    }

    public void setResponse(Customers response) {
        this.response = response;
    }

    public List<Customer> getItems() {
        return items;
    }

    public Customers setItems(List<Customer> items) {
        this.items = items;
        return this;
    }
}
