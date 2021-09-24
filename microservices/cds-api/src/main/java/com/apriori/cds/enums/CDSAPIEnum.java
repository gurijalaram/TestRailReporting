package com.apriori.cds.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CDSAPIEnum implements ExternalEndpointEnum {

    //AP
    GET_AP_VERSION("ap-versions"),

    // APPLICATION
    GET_APPLICATION("applications"),
    GET_APPLICATION_BY_ID("applications/%s"),
    GET_CUSTOMER_APPLICATION_BY_ID("applications/%s/customers"),

    // CONFIGURATIONS
    GET_CONFIGURATIONS_EMAIL("configurations/blacklisted-email-domains"),

    // LICENSE
    GET_LICENSES_BY_CUSTOMER_ID("customers/%s/licenses"),
    GET_SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID("customers/%s/licenses/%s"),
    GET_SUB_LICENSES("customers/%s/sites/%s/licenses/%s/sub-licenses"),
    GET_SPECIFIC_SUB_LICENSE("customers/%s/sites/%s/licenses/%s/sub-licenses/%s"),
    GET_SPECIFIC_SUB_LICENSE_USERS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),
    DELETE_SPECIFIC_USER_SUB_LICENSE_USERS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s"),


    // CUSTOMER ASSOCIATIONS
    GET_CUSTOMERS_ASSOCIATIONS_BY_CUSTOMER_ID("customers/%s/customer-associations"),
    GET_SPECIFIC_CUSTOMERS_ASSOCIATION_BY_CUSTOMER_ASSOCIATION_ID("customers/%s/customer-associations/%s"),

    //CUSTOMERS
    GET_CUSTOMERS("customers"),
    DELETE_CUSTOMER_BY_CUSTOMER_ID("customers/%s"),
    GET_CUSTOMER_ASSOCIATIONS("customers/%s/customer-associations/%s/customer-association-users/%s"),
    DELETE_CUSTOMER_ASSOCIATIONS("customers/%s/customer-associations/%s/customer-association-users/%s"),
    POST_CUSTOMERS("customers"),
    GET_CUSTOMERS_ASSOCIATIONS_WITH_PAGE_BY_ID("customers/%s/customer-associations&pageSize=5000"),
    PATCH_CUSTOMERS_BY_ID("customers/%s"),

    //USERS,
    POST_USERS("customers/%s/users"),
    DELETE_USER_BY_CUSTOMER_ID("customers/%s/users/L2H992829CFB"),
    PATCH_USERS_BY_CUSTOMER_USER_IDS("customers/%s/users/%s"),
    DELETE_USERS_BY_CUSTOMER_USER_IDS("customers/%s/users/%s"),

    //SITES
    POST_SITES_BY_CUSTOMER_ID("customers/%s/sites"),

    //DEPLOYMENTS
    POST_DEPLOYMENTS_BY_CUSTOMER_ID("customers/%s/deployments"),

    //APPLICATION SITES
    POST_APPLICATION_SITES_BY_CUSTOMER_SITE_IDS("customers/%s/sites/%s/licensed-applications"),

    //INSTALLATIONS
    POST_INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS("customers/%s/deployments/%s/installations"),
    PATCH_INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS("customers/%s/deployments/%s/installations/%s"),

    //ASSOCIATIONS
    POST_ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS("customers/%s/customer-associations/%s/customer-association-users"),
    POST_SUBLICENSE_ASSOCIATIONS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),

    //SAML
    POST_SAML_BY_CUSTOMER_ID("customers/%s/identity-providers"),
    PATCH_SAML_BY_CUSTOMER_PROVIDER_IDS("customers/%s/identity-providers/%s"),

    //LICENSE
    POST_LICENSE_BY_CUSTOMER_SITE_IDS("customers/%s/sites/%s/licenses"),

    //ACCESS CONTROL
    POST_ACCESS_CONTROL_BY_CUSTOMER_USER_IDS("customers/%s/users/%s/access-controls");

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
