package com.apriori.cas.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMER(CUSTOMERS.getEndpointString().concat("/%s")),

    //CUSTOMER ASSOCIATIONS
    CUSTOMER_ASSOCIATIONS(CUSTOMER.getEndpointString().concat("/customer-associations")),
    CUSTOMER_ASSOCIATION(CUSTOMER_ASSOCIATIONS.getEndpointString().concat("/%s")),

    //CUSTOMER ASSOCIATION CANDIDATES
    CUSTOMER_ASSOCIATION_CANDIDATES(CUSTOMER_ASSOCIATION.getEndpointString().concat("/customer-association-candidates")),

    //CUSTOMER ASSOCIATION USERS
    CUSTOMER_ASSOCIATIONS_USERS(CUSTOMER_ASSOCIATION.getEndpointString().concat("/customer-association-users")),
    CUSTOMER_ASSOCIATIONS_USER(CUSTOMER_ASSOCIATIONS_USERS.getEndpointString().concat("/%s")),

    //SITES
    SITES(CUSTOMER.getEndpointString().concat("/sites")),
    SITE_ID(SITES.getEndpointString().concat("/%s")),

    //APPLICATIONS
    CUSTOMER_APPLICATIONS(CUSTOMER.getEndpointString().concat("/applications")),
    CUSTOMER_LICENSED_APP(CUSTOMER.getEndpointString().concat("/licensed-applications")),

    //LICENSES
    LICENSE_BY_CUSTOMER_SITE_IDS(SITE_ID.getEndpointString().concat("/licenses")),
    LICENSE_BY_ID(LICENSE_BY_CUSTOMER_SITE_IDS.getEndpointString().concat("/%s")),
    ACTIVATE_LICENSE(LICENSE_BY_ID.getEndpointString().concat("/activate")),
    SUBLICENSES_BY_LICENSE_ID(LICENSE_BY_ID.getEndpointString().concat("/sub-licenses")),
    SUBLICENSE_BY_ID(SUBLICENSES_BY_LICENSE_ID.getEndpointString().concat("/%s")),
    SUBLICENSE_ASSOCIATIONS(SUBLICENSE_BY_ID.getEndpointString().concat("/users")),
    SPECIFIC_USER_SUB_LICENSE_USERS(SUBLICENSE_ASSOCIATIONS.getEndpointString().concat("/%s")),

    //MFA
    MFA(CUSTOMER.getEndpointString().concat("/reset-mfa")),

    //USERS
    USERS(CUSTOMER.getEndpointString().concat("/users")),
    USER(USERS.getEndpointString().concat("/%s")),
    USERS_SUBLICENSES(USER.getEndpointString().concat("/licensing")),
    EXPORT_TEMPLATE(USERS.getEndpointString().concat("/export-template")),
    EXPORT_USERS(USERS.getEndpointString().concat("/export")),

    //BATCHES
    BATCHES(CUSTOMER.getEndpointString().concat("/batches")),
    BATCH(BATCHES.getEndpointString().concat("/%s")),
    BATCH_ITEMS(BATCH.getEndpointString().concat("/items")),
    BATCH_ITEM(BATCH_ITEMS.getEndpointString().concat("/%s")),

    //CONFIGURATIONS
    CONFIGURATIONS("configurations/ap-versions"),

    //CUSTOMER DEPLOYMENTS
    DEPLOYMENTS(CUSTOMER.getEndpointString().concat("/deployments")),
    CUSTOMER_DEPLOYMENT(DEPLOYMENTS.getEndpointString().concat("/%s")),

    //CURRENT USER
    CURRENT_USER("users/current"),

    //CUSTOMER USER ACCESS CONTROLS
    ACCESS_CONTROLS(USER.getEndpointString().concat("/access-controls")),
    ACCESS_CONTROL_BY_ID(ACCESS_CONTROLS.getEndpointString().concat("/%s")),

    //BULK GRANT/DENY ACCESS TO APPLICATION
    GRANT_DENY_ALL(SITE_ID.getEndpointString().concat("/deployments/%s/installations/%s/applications/%s/%s")),

    //ACCESS AUTHORIZATIONS
    ACCESS_AUTHORIZATIONS(CUSTOMER.getEndpointString().concat("/access-authorizations")),
    ACCESS_AUTHORIZATION_BY_ID(ACCESS_AUTHORIZATIONS.getEndpointString().concat("/%s")),

    //CUSTOMER SERVICE ACCOUNTS
    SERVICE_ACCOUNTS(CUSTOMER.getEndpointString().concat("/service-accounts"));

    private final String endpoint;

    CASAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cas.api_url") + String.format(getEndpointString(), variables);
    }
}