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

    // PROCESS GROUP
    PROCESS_GROUPS("process-groups"),
    PROCESS_GROUPS_WITH_KEY_PARAM("digital-factories?key=%s"),
    PROCESS_GROUPS_BY_PATH("process-groups/%s"),
    PROCESS_GROUPS_BY_PATH_WITH_KEY_PARAM("process-groups/%s?key=%s");

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
