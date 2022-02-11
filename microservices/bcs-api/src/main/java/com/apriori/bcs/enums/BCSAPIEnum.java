package com.apriori.bcs.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum BCSAPIEnum implements ExternalEndpointEnum {

    // REPORTS
    REPORTS("reports"),
    CUSTOMERS(""),
    REPORT_BY_ID("reports/%s"),
    REPORT_EXPORT_BY_ID("reports/%s/export"),
    REPORT_TEMPLATES("report-templates"),
    REPORT_TEMPLATES_PART_REPORT("report-templates&type[EQ]=PART_REPORT"),

    // BATCHES
    BATCHES("batches"),
    BATCH_BY_ID("batches/%s"),

    // PARTS
    BATCH_PARTS_BY_ID("batches/%s/parts"),
    BATCH_PARTS("batches/parts"),
    BATCH_PART_BY_BATCH_PART_IDS("batches/%s/parts/%s"),
    RESULTS_BY_BATCH_PART_IDS("batches/%s/parts/%s/results"),
    PART_REPORT_BY_BATCH_PART_IDS("batches/%s/parts/%s/part-report"),

    //COSTING
    START_COSTING_BY_ID("batches/%s/start-costing"),
    CANCEL_COSTING_BY_ID("batches/%s/cancel"),

    //CUSTOMERS
    BATCH_PARTS_BY_CUSTOMER_ID("%s/batches/%s/parts"),
    BATCH_PART_BY_CUSTOMER_BATCH_PART_IDS("%s/batches/%s/parts/%s"),
    RESULTS_BY_CUSTOMER_BATCH_PART_IDS("%s/batches/%s/parts/%s/results"),
    PART_REPORT_BY_CUSTOMER_BATCH_PART_IDS("%s/batches/%s/parts/%s/part-report"),
    BATCH_PARTS_BY_CUSTOMER_BATCH_ID("%s/batches/%s/parts"),

    GET_VPEs("virtual-production-environments"),
    GET_DIGITAL_FACTORIES("digital-factories"),
    GET_CUSTOM_ATTRIBUTES("custom-attributes"),
    GET_PROCESS_GROUPS("process-groups"),
    GET_USER_DEFINED_ATTRIBUTES("user-defined-attributes"),
    GET_COSTING_PREFERENCES("costing-preferences"),
    PATCH_COSTING_PREFERENCES("costing-preferences");


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
        return PropertiesContext.get("${env}.bcs.api_url") + "customers/" + PropertiesContext.get("${env}.customer_identity") + "/" +
                String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
