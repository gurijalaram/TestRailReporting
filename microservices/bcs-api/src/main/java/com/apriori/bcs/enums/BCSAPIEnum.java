package com.apriori.bcs.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum BCSAPIEnum implements ExternalEndpointEnum {

    // REPORTS
    REPORTS("%s/reports"),
    CUSTOMERS(""),
    REPORT_BY_ID("%s/reports/%s"),
    REPORT_EXPORT_BY_ID("%s/reports/%s/export"),
    REPORT_TEMPLATES("%s/report-templates"),
    REPORT_TEMPLATES_PART_REPORT("report-templates&type[EQ]=PART_REPORT"),

    // BATCHES
    BATCHES("%s/batches"),
    BATCH_BY_ID("%s/batches/%s"),

    // PARTS
    BATCH_PARTS_BY_ID("%s/batches/%s/parts?pageSize"),
    BATCH_PARTS("%s/batches/parts"),
    BATCH_PART_BY_BATCH_PART_IDS("%s/batches/%s/parts/%s"),
    RESULTS_BY_BATCH_PART_IDS("%s/batches/%s/parts/%s/results"),
    PART_REPORT_BY_BATCH_PART_IDS("%s/batches/%s/parts/%s/part-report"),
    BATCH_PARTS_BY_CUSTOMER_BATCH_ID("%s/batches/%s/parts"),

    //COSTING
    START_COSTING_BY_ID("%s/batches/%s/start-costing"),
    CANCEL_COSTING_BY_ID("%s/batches/%s/cancel"),

    //CUSTOMERS
    CUSTOMER_VPEs("%s/virtual-production-environments"),
    CUSTOMER_DIGITAL_FACTORIES("%s/digital-factories"),
    CUSTOMER_CUSTOM_ATTRIBUTES("%s/custom-attributes"),
    CUSTOMER_PROCESS_GROUPS("%s/process-groups"),
    CUSTOMER_USER_DEFINED_ATTRIBUTES("%s/user-defined-attributes"),
    CUSTOMER_USER_PREFERENCES("%s/user-preferences"),
    CUSTOMER_USER_PREFERENCES_NO_CUSTOMER("user-preferences");

    private final String endpoint;

    BCSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {

        return PropertiesContext.get("bcs.api_url") + "customers/"
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
