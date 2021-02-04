package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "cas/SingleCustomerSchema.json")
public class SingleCustomerResponse {

    @JsonProperty("response")
    public SingleCustomer getResponse() {
        return response;
    }

    public void setResponse(SingleCustomer response) {
        this.response = response;
    }

    private SingleCustomer response;
}
