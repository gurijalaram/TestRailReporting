package com.apriori.cas.api;

import static com.apriori.shared.util.enums.CustomerEnum.AP_INT;

import com.apriori.shared.util.properties.PropertiesContext;

class APCustomerCheck {

    /**
     * Check to see if customer is apriori-internal
     *
     * @return - true/false
     */
    static boolean APCustomer() {
        return PropertiesContext.get("customer").equals(AP_INT);
    }
}
