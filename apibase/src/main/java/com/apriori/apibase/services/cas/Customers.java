package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/CustomersSchema.json")
public class Customers extends Pagination {
    private List<Customer> items;
    private Customers response;

    public Customers getResponse() {
        return this.response;
    }

    public List<Customer> getItems() {
        return this.items;
    }
}
