package com.apriori.cds.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CDSAPIEnum implements ExternalEndpointEnum {
    //CUSTOMERS
    GET_CUSTOMERS("customers"),

    //USERS
    POST_USERS("customers/%s/users"),
    PATCH_USERS("customers/%s/users/%s"),

    //SITES
    POST_SITES("customers/%s/sites"),

    //DEPLOYMENTS
    POST_DEPLOYMENTS("customers/%s/deployments"),

    //APPLICATION SITES
    POST_APPLICATION_SITES("customers/%s/sites/%s/licensed-applications"),

    //INSTALLATIONS
    POST_INSTALLATIONS("customers/%s/deployments/%s/installations"),
    PATCH_INSTALLATIONS("customers/%s/deployments/%s/installations/%s"),

    //ASSOCIATIONS
    POST_ASSOCIATIONS("customers/%s/customer-associations/%s/customer-association-users"),
    POST_SUBLICENSE_ASSOCIATIONS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),

    //SAML
    POST_SAML("customers/%s/identity-providers"),
    PATCH_SAML("customers/%s/identity-providers/%s"),

    //LICENSE
    POST_LICENSE("customers/%s/sites/%s/licenses"),

    //ACCESS CONTROL
    POST_ACCESS_CONTROL("customers/%s/users/%s/access-controls");

    private final String endpoint;

    CDSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cds.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
