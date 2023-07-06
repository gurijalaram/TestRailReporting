package com.apriori.apibase.services.cas;

import com.apriori.utils.Pagination;
import com.apriori.utils.common.objects.Paged;
import com.apriori.utils.http.enums.Schema;

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
