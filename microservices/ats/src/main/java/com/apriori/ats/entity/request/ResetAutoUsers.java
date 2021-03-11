package com.apriori.ats.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetAutoUsers {
    private ResetAutoUsers response;
    private String password;

    public ResetAutoUsers getResponse() {
        return response;
    }

    public ResetAutoUsers setResponse(ResetAutoUsers response) {
        this.response = response;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ResetAutoUsers setPassword(String password) {
        this.password = password;
        return this;
    }
}
