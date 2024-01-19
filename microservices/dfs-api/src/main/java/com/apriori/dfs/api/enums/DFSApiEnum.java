package com.apriori.dfs.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.Getter;
import lombok.Setter;

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
    PROCESS_GROUPS_BY_PATH_WITH_KEY_PARAM("process-groups/%s?key=%s"),

    // MATERIALS
    MATERIALS("digital-factories/%s/process-groups/%s/materials"),
    MATERIALS_WITH_KEY_PARAM("digital-factories/%s/process-groups/%s/materials?key=%s"),
    MATERIALS_BY_NAME("digital-factories/%s/process-groups/%s/materials?name[%s]=%s"),
    MATERIALS_SORTED_BY_NAME("digital-factories/%s/process-groups/%s/materials?pageSize=%s&pageNumber=%s&sortBy[%s]=name"),
    MATERIALS_WITH_PAGE_SIZE_AND_PAGE_NUMBER("digital-factories/%s/process-groups/%s/materials?pageSize=%s&pageNumber=%s"),
    MATERIAL_BY_PATH("digital-factories/%s/process-groups/%s/materials/%s"),
    MATERIAL_BY_PATH_WITH_KEY_PARAM("digital-factories/%s/process-groups/%s/materials/%s?key=%s"),

    // MATERIAL STOCKS
    MATERIAL_STOCKS_BY_PATH("digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s"),
    MATERIAL_STOCKS_BY_PATH_WITH_KEY_PARAM("digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s?key=%s");
    private final String endpoint;

    @Getter
    @Setter
    private Boolean withSharedSecret;

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
        String sharedSecretKey = "key=";
        if (Boolean.FALSE.equals(withSharedSecret)
            || Arrays.stream(variables).anyMatch(o -> o.toString().contains(sharedSecretKey) || o.toString().isEmpty())) {

            return endpoint;
        }

        return endpoint + this.addQuery(getEndpointString());
    }
}
