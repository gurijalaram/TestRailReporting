package main.java.http.enums.common.api;

import main.java.http.enums.common.EdcQaAPI;

public enum BillOfMaterialsAPIEnum implements EdcQaAPI {

    GET_ACCOUNTS_STATUS("accounts/active"),
    GET_BILL_OF_METERIALS("bill-of-materials"),
    POST_BILL_OF_METERIALS("bill-of-materials"),
    DELETE_BILL_OF_METERIALS_IDENTITY("bill-of-materials/%s"),
    GET_BILL_OF_METERIALS_IDENTITY("bill-of-materials/%s"),
    EXPORT_BILL_OF_METERIALS_IDENTITY("bill-of-materials/%s/export"),
    POST_BILL_OF_METERIALS_IDENTITY("bill-of-materials/%s")
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
