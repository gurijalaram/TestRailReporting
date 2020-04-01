package com.apriori.utils.http.enums.common.api;

import com.apriori.utils.http.enums.common.EdcQaAPI;

public enum AccountEndpointEnum implements EdcQaAPI {

    GET_ACCOUNTS("accounts"),
    POST_ACCOUNTS("accounts"),
    GET_ACTIVE_USER("accounts/active"),
    DELETE_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    GET_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    UPDATE_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    ACTIVATE_ACCOUNTS_BY_IDENTITY("accounts/%s/activate");

    private final String endpoint;

    AccountEndpointEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }
}
