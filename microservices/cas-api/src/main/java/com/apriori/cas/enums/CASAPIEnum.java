package com.apriori.cas.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMER(CUSTOMERS.getEndpointString().concat("/%s")),
    GET_CUSTOMERS(CUSTOMERS.getEndpointString().concat("/%s/%s")),

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

    //LICENSES
    LICENSE_BY_CUSTOMER_SITE_IDS(SITE_ID.getEndpointString().concat("/licenses")),
    SUBLICENSES_BY_LICENSE_ID(LICENSE_BY_CUSTOMER_SITE_IDS.getEndpointString().concat("/%s/sub-licenses")),
    SUBLICENSE_ASSOCIATIONS(SUBLICENSES_BY_LICENSE_ID.getEndpointString().concat("/%s/users")),
    SPECIFIC_USER_SUB_LICENSE_USERS(SUBLICENSE_ASSOCIATIONS.getEndpointString().concat("/%s")),

    //MFA
    MFA(CUSTOMER.getEndpointString().concat("/reset-mfa")),

    //USERS
    USERS(CUSTOMER.getEndpointString().concat("/users")),
    USER(USERS.getEndpointString().concat("/%s")),

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
    ACCESS_CONTROL_BY_ID(ACCESS_CONTROLS.getEndpointString().concat("/%s"));

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
        return PropertiesContext.get("${env}.cas.api_url") + String.format(getEndpointString(), variables);
    }
}