package com.apriori.cas.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMER(CUSTOMERS.getEndpointString().concat("/%s")),
    GET_CUSTOMERS(CUSTOMERS.getEndpointString().concat("/%s/%s")),
    /**
     * @deprecated Just use CUSTOMER
     */
    GET_CUSTOMER_ID(CUSTOMER.getEndpointString()),
    /**
     * @deprecated Just use CUSTOMERS
     */
    GET_CUSTOMER(CUSTOMERS.getEndpointString().concat("/")),

    //CUSTOMER ASSOCIATIONS
    CUSTOMER_ASSOCIATIONS(CUSTOMER.getEndpointString().concat("/customer-associations")),
    CUSTOMER_ASSOCIATION(CUSTOMER_ASSOCIATIONS.getEndpointString().concat("/%s")),

    //CUSTOMER ASSOCIATION CANDIDATES
    CUSTOMER_ASSOCIATION_CANDIDATES(CUSTOMER_ASSOCIATION.getEndpointString().concat("/customer-association-candidates")),

    //CUSTOMER ASSOCIATION USERS
    CUSTOMER_ASSOCIATIONS_USERS(CUSTOMER_ASSOCIATION.getEndpointString().concat("/customer-association-users")),
    CUSTOMER_ASSOCIATIONS_USER(CUSTOMER_ASSOCIATIONS_USERS.getEndpointString().concat("/%s")),

    //SITES
    GET_SITES(CUSTOMER.getEndpointString().concat("/sites")),
    GET_SITE_ID(GET_SITES.getEndpointString().concat("/%s")),
    POST_SITES(CUSTOMER.getEndpointString().concat("/sites")),
    POST_SITES_ID(POST_SITES.getEndpointString().concat("/%s")),

    //APPLICATIONS
    GET_CUSTOMER_APPLICATIONS(CUSTOMER.getEndpointString().concat("/applications")),

    //LICENSES
    DELETE_SPECIFIC_USER_SUB_LICENSE_USERS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s"),
    POST_LICENSE_BY_CUSTOMER_SITE_IDS(POST_SITES_ID.getEndpointString().concat("/licenses")),
    GET_LICENSES_BY_CUSTOMER_ID(POST_SITES_ID.getEndpointString().concat("/licenses")),
    GET_SUBLICENSES_BY_LICENSE_ID(GET_LICENSES_BY_CUSTOMER_ID.getEndpointString().concat("/%s/sub-licenses")),
    POST_SUBLICENSE_ASSOCIATIONS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),
    GET_SUBLICENSE_ASSOCIATIONS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),


    //MFA
    POST_MFA(GET_CUSTOMER.getEndpointString().concat("%s/reset-mfa")),

    //USERS
    USERS(CUSTOMER.getEndpointString().concat("/users")),
    USER(USERS.getEndpointString().concat("/%s")),
    DELETE_USERS_BY_CUSTOMER_USER_IDS("customers/%s/users/%s"),

    /**
     * @deprecated Just use USER
     */
    GET_USERS(USER.getEndpointString()),
    /**
     * @deprecated Just use USER
     */
    PATCH_USERS(USER.getEndpointString()),

    //BATCHES
    GET_BATCHES(CUSTOMER.getEndpointString().concat("/batches")),
    GET_BATCH(GET_BATCHES.getEndpointString().concat("/%s")),
    BATCH_ITEMS(GET_BATCH.getEndpointString().concat("/items")),
    BATCH_ITEM(BATCH_ITEMS.getEndpointString().concat("/%s")),

    //CONFIGURATIONS
    GET_CONFIGURATIONS("configurations/ap-versions"),

    //CUSTOMER DEPLOYMENTS
    GET_DEPLOYMENTS(CUSTOMER.getEndpointString().concat("/deployments")),
    GET_CUSTOMER_DEPLOYMENT(GET_DEPLOYMENTS.getEndpointString().concat("/%s")),

    //CURRENT USER
    GET_CURRENT_USER("users/current");

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