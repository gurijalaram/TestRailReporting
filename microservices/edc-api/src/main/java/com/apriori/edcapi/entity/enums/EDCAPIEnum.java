package com.apriori.edcapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum EDCAPIEnum implements ExternalEndpointEnum {

    //Accounts
    GET_ACCOUNTS("accounts"),
    POST_ACCOUNTS("accounts"),
    GET_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    DELETE_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    PATCH_ACCOUNTS_BY_IDENTITY("accounts/%s"),
    POST_ACCOUNTS_BY_IDENTITY("accounts/%s/activate"),
    GET_CURRENT_ACTIVE_ACCOUNT("accounts/active"),

    //Bill Of Materials
    GET_BILL_OF_MATERIALS("bill-of-materials"),
    POST_BILL_OF_MATERIALS("bill-of-materials"),
    GET_BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s"),
    DELETE_BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s"),
    POST_BILL_OF_MATERIALS_IDENTITY_TO_EXPORT("bill-of-materials/%s/export"),

    //Line Items
    GET_LINE_ITEMS("bill-of-materials/%s/line-items"),

    //Parts
    GET_BILL_OF_MATERIALS_LINE_ITEMS_PARTS("bill-of-materials/%s/line-items/%s/parts"),
    POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS("bill-of-materials/%s/line-items/%s/parts"),

    PATCH_BILL_OF_MATERIALS_LINE_ITEMS_PARTS("bill-of-materials/%s/line-items/%s/parts/%s"),
    POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_EXPORT("bill-of-materials/%s/line-items/%s/parts/%S/select"),
    POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_COST("bill-of-materials/%s/line-items/%s/parts/cost"),

    //Reports
    POST_REPORTS("reports"),

    //Users
    GET_CURRENT_USER("users/current");

    private final String endpoint;

    EDCAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.edc.api_url") + String.format(getEndpointString(), variables);
    }
}
