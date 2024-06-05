package com.apriori.shared.util.enums;

import lombok.Getter;

@Getter
public enum CustomerEnum {

    AP_INT("apriori-internal"),
    WIDGETS("widgets");

    private final String customer;

    CustomerEnum(String customer) {
        this.customer = customer;
    }
}
