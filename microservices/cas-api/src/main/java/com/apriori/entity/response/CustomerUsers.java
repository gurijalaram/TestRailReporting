package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/CustomerUsersSchema.json")
public class CustomerUsers extends Pagination {
    private List<CustomerUser> items;
    private CustomerUsers response;

    public CustomerUsers getResponse() {
        return this.response;
    }

    public List<CustomerUser> getItems() {
        return this.items;
    }
}
