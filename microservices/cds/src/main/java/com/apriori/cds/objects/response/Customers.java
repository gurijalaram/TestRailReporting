package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/CustomersSchema.json")
public class Customers extends Pagination {
    private List<Customer> items;
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
