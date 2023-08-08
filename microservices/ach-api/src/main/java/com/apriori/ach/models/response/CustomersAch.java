package com.apriori.ach.models.response;

import com.apriori.annotations.Schema;
import com.apriori.interfaces.Paged;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomersAchSchema.json")
@Data
@JsonRootName("response")
public class CustomersAch extends Pagination implements Paged<CustomerAch> {
    private List<CustomerAch> items;
}