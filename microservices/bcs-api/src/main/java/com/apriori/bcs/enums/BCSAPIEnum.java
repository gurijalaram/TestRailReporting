package com.apriori.bcs.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum BCSAPIEnum implements ExternalEndpointEnum {

    // REPORTS
    GET_CUSTOMERS(""),
    GET_REPORTS("reports"),
    GET_REPORT_BY_ID("reports/%s"),
    GET_REPORT_EXPORT_BY_ID("reports/%s/export"),
    GET_REPORT_TEMPLATES("report-templates"),
    GET_REPORT_TEMPLATES_PART_REPORT("report-templates&type[EQ]=PART_REPORT"),
    POST_REPORTS("reports"),

    // BATCHES
    GET_BATCH_PARTS("batches/%sparts"),
    GET_BATCH_BY_ID("batches/%s"),
    GET_BATCHES("batches"),
    GET_BATCH_PART_BY_BATCH_PART_IDS("batches/%sparts/%s"),
    GET_RESULTS_BY_BATCH_PART_IDS("batches/%sparts/%sresults"),
    GET_PART_REPORT_BY_BATCH_PART_IDS("batches/%sparts/%spart-report"),
    POST_START_COSTING_BY_ID("batches/%s/start-costing"),
    POST_CANCEL_COSTING_BY_ID("batches/%s/cancel"),
    POST_BATCHES("batches"),
    POST_BATCH_PARTS_BY_ID("batches/%sparts"),

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

    @Override
    public String getEndpoint(String customer, Object... variables) {
        if (!customer.isEmpty()) {
            customer = customer + "/";
        }

        return PropertiesContext.get("${env}.bcs.api_url") + "customers/" + customer +
                String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
