package com.apriori.ats.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetAutoUsers {
    private String password;

    public String getPassword() {
        return password;
    }

    public ResetAutoUsers setPassword(String password) {
        this.password = password;
        return this;
    }
}
