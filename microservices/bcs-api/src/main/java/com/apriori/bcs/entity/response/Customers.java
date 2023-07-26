package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CustomersItemsResponseSchema.json")
public class Customers extends Pagination {
    private List<Customer> items;
}
