package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "AccountsSchema.json")
public class Accounts extends BasePageResponse {

    @JsonProperty("items")
    private List<AccountStatus> accountStatuses;

    public List<AccountStatus> getAccountStatuses() {
        return accountStatuses;
    }

    public Accounts setAccountStatuses(List<AccountStatus> accountStatuses) {
        this.accountStatuses = accountStatuses;
        return this;
    }
}
