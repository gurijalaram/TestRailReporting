package com.apriori.enums;

import com.apriori.interfaces.EdcQaAPI;

public enum BillOfMaterialsAPIEnum implements EdcQaAPI {

    GET_ACCOUNTS_STATUS("accounts/active"),
    GET_BILL_OF_MATERIALS("bill-of-materials"),
    POST_BILL_OF_MATERIALS("bill-of-materials"),
    DELETE_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s"),
    GET_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s"),
    EXPORT_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s/export"),
    POST_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s");

    private final String endpoint;

    BillOfMaterialsAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
