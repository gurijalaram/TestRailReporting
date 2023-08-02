package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;
import com.apriori.interfaces.Paged;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerUsersSchema.json")
@JsonRootName("response")
@Data
public class CustomerUsers extends Pagination implements Paged<CustomerUser> {
    private List<CustomerUser> items;
}
