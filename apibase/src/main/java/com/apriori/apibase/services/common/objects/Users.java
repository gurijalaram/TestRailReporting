package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "common/UsersSchema.json")
public class Users extends Pagination {
    @JsonProperty
    private List<User> items;
    @JsonProperty
    private Users response;

    public Users getResponse() {
        return this.response;
    }

    public Users setResponse(Users response) {
        this.response = response;
        return this;
    }

    /*public Users setItems(List<User> items) {
        this.items = items;
        return this;
    }

    public List<User> getItems() {
        return this.items;
    }*/
}

