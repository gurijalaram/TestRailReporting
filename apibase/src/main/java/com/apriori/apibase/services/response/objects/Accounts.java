package com.apriori.apibase.services.response.objects;

import com.apriori.annotations.Schema;

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
