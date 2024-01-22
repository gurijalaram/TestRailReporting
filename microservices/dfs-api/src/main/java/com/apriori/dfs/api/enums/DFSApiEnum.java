package com.apriori.dfs.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

import java.util.Arrays;

public enum DFSApiEnum implements ExternalEndpointEnum {

    // DIGITAL FACTORY
    DIGITAL_FACTORIES("digital-factories"),
    DIGITAL_FACTORIES_WITH_KEY_PARAM("digital-factories?key=%s"),
    DIGITAL_FACTORIES_BY_PATH("digital-factories/%s"),
    DIGITAL_FACTORIES_BY_PATH_WITH_KEY_PARAM("digital-factories/%s?key=%s"),
    DIGITAL_FACTORIES_BY_NAME("digital-factories?name[%s]=%s"),
    DIGITAL_FACTORIES_SORTED_BY_NAME("digital-factories?pageSize=1000&pageNumber=1&sortBy[%s]=name"),
    DIGITAL_FACTORIES_WITH_PAGE_SIZE_AND_PAGE_NUMBER("digital-factories?pageSize=%s&pageNumber=%s"),

    // PROCESS GROUP
    PROCESS_GROUPS("process-groups"),
    PROCESS_GROUPS_WITH_KEY_PARAM("digital-factories?key=%s"),
    PROCESS_GROUPS_BY_PATH("process-groups/%s"),
    PROCESS_GROUPS_BY_PATH_WITH_KEY_PARAM("process-groups/%s?key=%s"),
    PROCESS_GROUPS_BY_NAME("process-groups?name[%s]=%s"),
    PROCESS_GROUPS_SORTED_BY_NAME("process-groups?pageSize=100&pageNumber=1&sortBy[%s]=name"),
    PROCESS_GROUPS_WITH_PAGE_SIZE_AND_PAGE_NUMBER("process-groups?pageSize=%s&pageNumber=%s"),

    // MATERIALS
    MATERIAL_BY_PATH("digital-factories/%s/process-groups/%s/materials/%s"),
    MATERIAL_BY_PATH_WITH_KEY_PARAM("digital-factories/%s/process-groups/%s/materials/%s?key=%s"),

    // MATERIAL STOCKS
    MATERIAL_STOCKS_BY_PATH("digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s"),
    MATERIAL_STOCKS_BY_PATH_WITH_KEY_PARAM("digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s?key=%s");
    private final String endpoint;

    DFSApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {

        String endpoint = PropertiesContext.get("dfs.api_url") + String.format(getEndpointString(), variables);
        boolean dontAddSharedSecret = Arrays.stream(variables)
            .map(Object::toString)
            .anyMatch(String::isEmpty);

        return dontAddSharedSecret ? endpoint : endpoint + this.addQuery(getEndpointString());
    }
}
