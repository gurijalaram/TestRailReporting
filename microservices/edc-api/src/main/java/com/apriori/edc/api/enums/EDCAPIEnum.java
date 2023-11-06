package com.apriori.edc.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum EDCAPIEnum implements ExternalEndpointEnum {

    //Accounts
    ACCOUNTS("accounts"),
    ACCOUNT_BY_IDENTITY("accounts/%s"),
    ACTIVATE_ACCOUNT_BY_IDENTITY("accounts/%s/activate"),
    CURRENT_ACTIVE_ACCOUNT("accounts/active"),
    REFRESH_LICENSE_BY_IDENTITY("accounts/%s/license/refresh"),

    //Bill Of Materials
    BILL_OF_MATERIALS("bill-of-materials"),
    BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s"),
    BILL_OF_MATERIALS_BY_IDENTITY_TO_EXPORT("bill-of-materials/%s/export"),

    //Line Items
    LINE_ITEMS("bill-of-materials/%s/line-items"),

    //Parts
    BILL_OF_MATERIALS_LINE_ITEMS_PARTS("bill-of-materials/%s/line-items/%s/parts"),

    BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_UPDATE("bill-of-materials/%s/line-items/%s/parts/%s"),
    BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_EXPORT("bill-of-materials/%s/line-items/%s/parts/%s/select"),
    BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_COST("bill-of-materials/%s/line-items/%s/parts/cost"),

    //Users
    CURRENT_USER("users/current");

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
        return PropertiesContext.get("edc.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
