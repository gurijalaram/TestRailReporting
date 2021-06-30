package com.apriori.vds.entity.enums;

import com.apriori.utils.http.enums.common.EdcQaAPI;
import com.apriori.vds.utils.Constants;

public enum VDSAPIEnum implements EdcQaAPI {

    // Access Controls
    GET_GROUPS("groups"),
    GET_PERMISSIONS("permissions"),

    POST_SYNCHRONIZE("synchronize"),


    // Configurations
    GET_CONFIGURATIONS("configurations"),
    GET_CONFIGURATIONS_BY_IDENTITY("configurations/%s"),

    PUT_CONFIGURATION("configurations"),

    // Custom Attributes
    GET_CUSTOM_ATTRIBUTES("custom-attributes"),

    // Customizations
    GET_CUSTOMIZATIONS("customizations"),


    // Digital Factories
    GET_DIGITAL_FACTORIES("digital-factories"),
    GET_DIGITAL_FACTORIES_BY_IDENTITY("digital-factories/%s"),
    GET_VPES("vpes"),
    GET_VPES_BY_IDENTITY("vpes/%s"),

    POST_DIGITAL_FACTORIES("digital-factories"),
    POST_VPES("vpes"),


    // Process Group Materials
    GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs("digital-factories/%s/process-groups/%s/materials"),
    GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs("digital-factories/%s/process-groups/%s/materials/%s"),


    // Process Group Materials Stocks
    GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs("digital-factories/%s/process-groups/%s/materials/%s/material-stocks"),
    GET_SPECIFIC_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs("digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s"),

    // Process Groups
    GET_PROCESS_GROUPS("process-groups"),
    GET_PROCESS_GROUP_BY_IDENTITY("process-groups/%s"),


    // Process Group Site Variables
    GET_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID("process-groups/%s/site-variables"),
    GET_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs("process-groups/%s/site-variables/%s"),
    PUT_PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID("process-groups/%s/site-variables"),
    DELETE_PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs("process-groups/%s/site-variables/%s"),
    POST_PROCESS_GROUP_SITE_VARIABLES_BY_PG_ID("process-groups/%s/site-variables"),
    PATCH_PROCESS_GROUP_SITE_VARIABLES_BY_PG_SITE_IDs("process-groups/%s/site-variables/%s"),

    // Site Variables
    GET_SITE_VARIABLES("site-variables"),
    GET_SITE_VARIABLE_BY_ID("site-variables/%s"),
    POST_SITE_VARIABLE("site-variables"),
    PUT_SITE_VARIABLES("site-variables"),
    DELETE_SITE_VARIABLE_BY_ID("site-variables/%s"),
    PATCH_SITE_VARIABLES_BY_ID("site-variables/%s"),

    // User Group Associations,
    GET_UG_ASSOCIATIONS_BY_GROUP_ID("groups/%s/user-group-associations"),
    POST_UG_ASSOCIATIONS_BY_GROUP_ID("groups/%s/user-group-associations"),
    GET_SPECIFIC_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs("groups/%s/user-group-associations/%s"),
    DELETE_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs("groups/%s/user-group-associations/%s"),
    PATCH_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs("groups/%s/user-group-associations/%s");


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