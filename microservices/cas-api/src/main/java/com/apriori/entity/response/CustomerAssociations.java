package com.apriori.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;
import com.apriori.interfaces.Paged;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerAssociationsSchema.json")
@JsonRootName("response")
@Data
public class CustomerAssociations extends Pagination implements Paged<CustomerAssociation> {
    private List<CustomerAssociation> items;
}
