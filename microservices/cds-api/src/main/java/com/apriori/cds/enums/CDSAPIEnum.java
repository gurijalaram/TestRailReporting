package com.apriori.cds.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CDSAPIEnum implements ExternalEndpointEnum {

    //AP
    AP_VERSION("ap-versions"),

    // APPLICATION
    APPLICATIONS("applications"),
    APPLICATION_BY_ID("applications/%s"),
    CUSTOMER_APPLICATION_BY_ID("applications/%s/customers"),
    CUSTOMER_LICENSED_APPLICATIONS_BY_IDS("customers/%s/sites/%s/licensed-applications/%s"),
    APPLICATION_SITES_BY_CUSTOMER_SITE_IDS("customers/%s/sites/%s/licensed-applications"),
    APPLICATION_INSTALLATION("customers/%s/deployments/%s/installations/%s/applications"),
    APPLICATION_INSTALLATION_BY_ID("customers/%s/deployments/%s/installations/%s/applications/%s"),

    // SITES
    SITES("sites"),
    SITE_BY_ID("sites/%s"),
    SITES_BY_CUSTOMER_ID("customers/%s/sites"),
    SITE_BY_CUSTOMER_SITE_ID("customers/%s/sites/%s"),


    // ROLES
    ROLES("roles"),
    ROLE_BY_ID("roles/%s"),
    USER_ROLES("customers/%s/users/%s/roles"),
    USER_ROLES_BY_ID("customers/%s/users/%s/roles/%s"),

    // CONFIGURATIONS
    CONFIGURATIONS_EMAIL("configurations/blacklisted-email-domains"),

    // LICENSE
    LICENSES_BY_CUSTOMER_ID("customers/%s/licenses"),
    SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID("customers/%s/licenses/%s"),
    SUB_LICENSES("customers/%s/sites/%s/licenses/%s/sub-licenses"),
    SPECIFIC_SUB_LICENSE("customers/%s/sites/%s/licenses/%s/sub-licenses/%s"),
    SUBLICENSE_ASSOCIATIONS_USERS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),
    SUBLICENSE_ASSOCIATIONS_USER_BY_ID("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s"),
    LICENSE_BY_CUSTOMER_SITE_IDS("customers/%s/sites/%s/licenses"),
    LICENSE_BY_CUSTOMER_SITE_LICENSE_IDS("customers/%s/sites/%s/licenses/%s"),
    LICENSE_ACTIVATE("customers/%s/sites/%s/licenses/%s/activate"),
    ACTIVE_MODULES("customers/%s/sites/%s/licenses/active/modules"),

    // CUSTOMER ASSOCIATIONS
    CUSTOMERS_ASSOCIATIONS("customers/%s/customer-associations?sortBy[DESC]=createdAt"),
    SPECIFIC_CUSTOMERS_ASSOCIATION_BY_CUSTOMER_ASSOCIATION_ID("customers/%s/customer-associations/%s"),
    CUSTOMER_ASSOCIATION_USER_BY_ID("customers/%s/customer-associations/%s/customer-association-users/%s"),
    ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS("customers/%s/customer-associations/%s/customer-association-users"),

    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMER_BY_ID("customers/%s"),
    CUSTOMERS_APPLICATION_BY_CUSTOMER_ID("customers/%s/applications"),

    //USERS
    USERS("users"),
    USER_BY_ID("users/%s"),
    USER_CREDENTIALS_BY_ID("users/%s/credentials"),
    USER_CREDENTIALS_BY_CUSTOMER_USER_IDS("customers/%s/users/%s/credentials"),
    CUSTOMER_USERS("customers/%s/users"),
    DELETE_USER_WRONG_ID("customers/%s/users/L2H992829CFB"),
    USER_BY_CUSTOMER_USER_IDS("customers/%s/users/%s"),
    USERS_LICENSES("customers/%s/users/%s/licensing"),
    REQUIRED_USER_PROPERTIES("customers/%s/users/%s/required-properties"),

    //DEPLOYMENTS
    DEPLOYMENTS_BY_CUSTOMER_ID("customers/%s/deployments"),
    DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS("customers/%s/deployments/%s"),

    //INSTALLATIONS
    INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS("customers/%s/deployments/%s/installations"),
    INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS("customers/%s/deployments/%s/installations/%s"),
    INSTALLATIONS("installations"),

    //FEATURE
    INSTALLATION_FEATURES("customers/%s/deployments/%s/installations/%s/features"),

    //SAML
    SAML_BY_CUSTOMER_ID("customers/%s/identity-providers"),
    SAML_BY_CUSTOMER_PROVIDER_IDS("customers/%s/identity-providers/%s"),

    //ACCESS CONTROL
    ACCESS_CONTROLS("customers/%s/users/%s/access-controls"),
    ACCESS_CONTROL_BY_ID("customers/%s/users/%s/access-controls/%s"),

    //CUSTOM_ATTRIBUTES
    CUSTOM_ATTRIBUTES("customers/%s/users/%s/custom-attributes"),
    CUSTOM_ATTRIBUTE_BY_ID("customers/%s/users/%s/custom-attributes/%s"),

    //USER_PREFERENCES
    USER_PREFERENCES("customers/%s/users/%s/preferences"),
    PREFERENCE_BY_ID("customers/%s/users/%s/preferences/%s"),

    //ACCESS_AUTHORIZATIONS
    ACCESS_AUTHORIZATIONS("customers/%s/access-authorizations"),
    ACCESS_AUTHORIZATION_BY_ID("customers/%s/access-authorizations/%s"),
    ACCESS_AUTHORIZATION_STATUS("customers/%s/access-authorizations/status");

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
        return PropertiesContext.get("cds.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
