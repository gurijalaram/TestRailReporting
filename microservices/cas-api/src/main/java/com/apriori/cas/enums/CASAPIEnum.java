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
    POST_SITES(CUSTOMER.getEndpointString().concat("/sites")),
    POST_SITES_ID(POST_SITES.getEndpointString().concat("/%s")),

    //LICENSES
    DELETE_SPECIFIC_USER_SUB_LICENSE_USERS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s"),
    POST_LICENSE_BY_CUSTOMER_SITE_IDS(POST_SITES_ID.getEndpointString().concat("/licenses")),
    GET_LICENSES_BY_CUSTOMER_ID(CUSTOMER.getEndpointString().concat("/licenses")),
    POST_SUBLICENSE_ASSOCIATIONS("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users"),


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
    GET_BATCHES(GET_CUSTOMER.getEndpointString().concat("%s/%s/%s/%s")),
    BATCH_ITEM(GET_BATCHES.getEndpointString().concat("/%s")),
    CUSTOMER_BATCHES(GET_CUSTOMER.getEndpointString().concat("%s/%s/%s")),

    //CONFIGURATIONS
    GET_CONFIGURATIONS("configurations/ap-versions"),

    //CUSTOMER DEPLOYMENTS
    GET_CUSTOMER_DEPLOYMENT(GET_CUSTOMER.getEndpointString().concat("deployments/%s")),

    //CURRENT USER
    GET_CURRENT_USER(USERS.getEndpointString().concat("current"));

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