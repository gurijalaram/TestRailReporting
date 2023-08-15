package com.apriori.enums;

import com.apriori.interfaces.EdcQaAPI;

public enum PartsAPIEnum implements EdcQaAPI {

    GET_LINE_ITEMS("bill-of-materials/%s/line-items"),
    GET_PARTS_BY_BILL_AND_LINE_IDENTITY("bill-of-materials/%s/line-items/%s/parts"),
    POST_PARTS_BY_BILL_AND_LINE_IDENTITY("bill-of-materials/%s/line-items/%s/parts"),
    POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY("bill-of-materials/%s/line-items/%s/parts/cost"),
    PATCH_UPDATE_PART_BY_BILL_LINE_AND_PART_IDENTITY("bill-of-materials/%s/line-items/%s/parts/%s"),
    POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY("bill-of-materials/%s/line-items/%s/parts/%s/select"),
    POST_AUTH("/auth/token");

    private final String endpoint;

    PartsAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }
}
