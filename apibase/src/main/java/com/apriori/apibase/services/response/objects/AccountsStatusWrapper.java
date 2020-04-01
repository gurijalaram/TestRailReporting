package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "AccountsStatusWrapperSchema.json")
public class AccountsStatusWrapper {

    @JsonProperty("response")
    private AccountStatus accountStatus;

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AccountsStatusWrapper setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }
}
