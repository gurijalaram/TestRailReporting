package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/CustomerUsersSchema.json")
public class Users extends Pagination {
    private List<User> items;
    private Users response;

    public Users getResponse() {
        return this.response;
    }

    public List<User> getItems() {
        return this.items;
    }
}

