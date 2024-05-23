package com.apriori.shared.util.enums;

import lombok.Getter;

@Getter
public enum CustomerEnum {

    CUSTOMER_AP_INT(CustomerEnum.AP_INT),
    CUSTOMER_WIDGETS(CustomerEnum.WIDGETS);

    public static final String AP_INT = "ap-int";
    public static final String WIDGETS = "widgets";

    private final String customer;

    CustomerEnum(String customer) {
        this.customer = customer;
    }
}
