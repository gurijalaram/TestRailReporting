package com.apriori.apibase.services.cas;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/ValidateSiteSchema.json")
public class ValidateSite {
    private ValidateSite response;
    private String status;

    public String getStatus() {
        return status;
    }

    public ValidateSite setStatus(String status) {
        this.status = status;
        return this;
    }

    public ValidateSite getResponse() {
        return response;
    }

    public ValidateSite setResponse(ValidateSite response) {
        this.response = response;
        return this;
    }
}