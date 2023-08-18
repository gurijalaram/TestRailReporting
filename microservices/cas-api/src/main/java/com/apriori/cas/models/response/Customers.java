package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.interfaces.Paged;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomersSchema.json")
@JsonRootName("response")
@Data
public class Customers extends Pagination implements Paged<Customer> {
    private List<Customer> items;
}