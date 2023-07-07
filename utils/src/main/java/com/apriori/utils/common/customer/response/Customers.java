package com.apriori.utils.common.customer.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.common.objects.Paged;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "CustomersSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customers extends Pagination implements Paged<Customer> {
    private List<Customer> items;
}
