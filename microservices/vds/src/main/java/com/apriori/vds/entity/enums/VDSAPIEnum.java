package com.apriori.vds.entity.enums;

import com.apriori.utils.http.enums.common.EdcQaAPI;
import com.apriori.vds.utils.Constants;

public enum VDSAPIEnum implements EdcQaAPI {

    // Access Controls
    GET_GROUPS("groups"),
    GET_PERMISSIONS("permissions"),

    POST_SYNCHRONIZE("synchronize"),


    //Configurations
    GET_CONFIGURATIONS("configurations"),
    GET_CONFIGURATIONS_BY_IDENTITY("configurations/%s"),

    PUT_CONFIGURATION("configurations"),

    //Custom Attributes
    GET_CUSTOM_ATTRIBUTES("custom-attributes"),

    //Customizations
    GET_CUSTOMIZATIONS("customizations"),


    // Site Variables
    GET_SITE_VARIABLES("site-variables");

    private final String endpoint;

    VDSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return Constants.getApiUrl() + String.format(getEndpointString(), variables) + "?key=" + Constants.getSecretKey();
    }

}
