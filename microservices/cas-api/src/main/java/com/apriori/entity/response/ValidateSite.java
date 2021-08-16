package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ValidateSiteSchema.json")
public class ValidateSite {
    private ValidateSite response;
    private String status;

    public String getStatus() {
        return status;
    }

    public ValidateSite getResponse() {
        return response;
    }
}