package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "cas/CasCustomersSchema.json")
public class Customers {
    @JsonProperty
    private Pagination response;

    public Pagination getResponse() {
        return this.response;
    }

    public Customers setResponse(Pagination response) {
        this.response = response;
        return this;
    }
}
