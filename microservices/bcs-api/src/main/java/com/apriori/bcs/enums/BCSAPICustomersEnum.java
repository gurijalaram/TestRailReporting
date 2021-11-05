package com.apriori.bcs.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum BCSAPICustomersEnum implements ExternalEndpointEnum {
    // BATCHES
    GET_BATCH_PARTS_BY_ID("batches/%s/parts"),
    GET_BATCH_PARTS("batches/parts"),
    GET_BATCH_PARTS_BY_CUSTOMER_ID("%s/batches/%s/parts"),

    GET_BATCH_PART_BY_CUSTOMER_BATCH_PART_IDS("%s/batches/%sparts/%s"),
    GET_BATCH_PART_BY_BATCH_PART_IDS("batches/%s/parts/%s"),
    GET_RESULTS_BY_BATCH_PART_IDS("%s/batches/%sparts/%sresults"),
    GET_PART_REPORT_BY_CUSTOMER_BATCH_PART_IDS("%s/batches/%sparts/%spart-report"),
    GET_PART_REPORT_BY_BATCH_PART_IDS("batches/%s/parts/%s/part-report"),
    POST_BATCH_PARTS_BY_CUSTOMER_BATCH_ID("%s/batches/%sparts"),
    POST_BATCH_PARTS_BY_ID("batches/%s/parts");

    private final String endpoint;

    BCSAPICustomersEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.bcs.api_url") + "customers/" +
                String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
