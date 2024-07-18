package com.apriori.vds.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum VDSAPIEnum implements ExternalEndpointEnum {

    // Access Controls
    GROUPS("/groups"),
    PERMISSIONS("/permissions"),
    SYNCHRONIZE("/synchronize"),

    // Configurations
    CONFIGURATIONS("/configurations"),
    CONFIGURATIONS_BY_IDENTITY("/configurations/%s"),
    CONFIGURATION("/configurations"),

    // Custom Attributes
    CUSTOM_ATTRIBUTES("/custom-attributes"),

    // Customizations
    CUSTOMIZATIONS("/customizations"),

    // Digital Factories
    DIGITAL_FACTORIES("/digital-factories"),
    DIGITAL_FACTORIES_BY_IDENTITY("/digital-factories/%s"),
    VPES("/vpes"),
    VPES_BY_IDENTITY("/vpes/%s"),

    // Process Group Materials
    PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_ID("/digital-factories/%s/process-groups/%s/materials"),
    SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_ID("/digital-factories/%s/process-groups/%s/materials/%s"),

    // Process Group Materials Stocks
    PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_ID("/digital-factories/%s/process-groups/%s/materials/%s/material-stocks"),
    PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_STOCK_ID("/digital-factories/%s/process-groups/%s/materials/%s/material-stocks/%s"),

    // Process Groups
    PROCESS_GROUPS("/process-groups"),
    PROCESS_GROUP_BY_IDENTITY("/process-groups/%s"),

    // Process Group Site Variables
    PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID("/process-groups/%s/site-variables"),
    PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_ID("/process-groups/%s/site-variables/%s"),

    // Process Group Associations
    PG_ASSOCIATIONS("/process-group-associations"),
    PG_ASSOCIATIONS_BY_ID("/process-group-associations/%s"),

    // Site Variables
    SITE_VARIABLES("/site-variables"),
    SITE_VARIABLE_BY_ID("/site-variables/%s"),

    // User Group Associations,
    USER_GROUP_ASSOCIATIONS_BY_GROUP_ID("/groups/%s/user-group-associations"),
    USER_GROUP_ASSOCIATIONS_BY_GROUP_USER_GROUP_ASSOCIATION_ID("/groups/%s/user-group-associations/%s");

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
        return PropertiesContext.get("vds.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
