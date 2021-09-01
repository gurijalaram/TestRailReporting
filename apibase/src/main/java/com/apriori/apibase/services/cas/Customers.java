package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomersSchema.json")
@Data
public class Customers extends Pagination {
    private List<Customer> items;
    private Customers response;
}
