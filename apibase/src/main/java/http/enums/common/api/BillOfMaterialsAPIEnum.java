package main.java.http.enums.common.api;

import main.java.http.enums.common.TemporaryAPI;

public enum BillOfMaterialsAPIEnum implements TemporaryAPI {

    GET_ACCOUNTS_STATUS("accounts/status"),
    GET_BILL_OF_MATERIALS("bill-of-materials"),
    POST_BILL_OF_MATERIALS("bill-of-materials"),
    DELETE_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s"),
    GET_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s"),
    EXPORT_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s/export"),
    POST_BILL_OF_MATERIALS_IDENTITY("bill-of-materials/%s"),
    POST_AUTH("/auth/token"),
    ;


    private final String endpoint;

    BillOfMaterialsAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
