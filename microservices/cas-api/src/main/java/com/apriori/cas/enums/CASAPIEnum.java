package com.apriori.cas.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    GET_CUSTOMERS("customers/%s/%s"),
    GET_CUSTOMER_ID("customers/%s"),
    GET_CUSTOMER("customers/"),

    //SITES
    POST_SITES(GET_CUSTOMER.getEndpointString().concat("%s/sites")),
    POST_SITES_ID(GET_CUSTOMER.getEndpointString().concat("%s/sites/%s")),

    //MFA
    POST_MFA(GET_CUSTOMER.getEndpointString().concat("%s/reset-mfa")),
    POST_MFA_CUSTOMER(GET_CUSTOMER.getEndpointString().concat("%s/%s/%s/reset-mfa")),

    //USERS
    POST_USERS(GET_CUSTOMER.getEndpointString().concat("%s/users/")),
    GET_USERS(GET_CUSTOMER.getEndpointString().concat("%s/users/%s")),
    PATCH_USERS(GET_CUSTOMER.getEndpointString().concat("%s/%users/%s")),

    //BATCHES
    POST_BATCH_ITEMS("/items/"),
    GET_BATCHES(GET_CUSTOMER.getEndpointString().concat("%s/%s/%s/%s")),
    BATCH_ITEM(GET_BATCHES.getEndpointString().concat("/%s")),
    CUSTOMER_BATCHES(GET_CUSTOMER.getEndpointString().concat("%s/%s/%s")),

    //CONFIGURATIONS
    GET_CONFIGURATIONS("configurations/ap-versions"),

    //CUSTOMER DEPLOYMENTS
    GET_CUSTOMER_DEPLOYMENT(GET_CUSTOMER.getEndpointString().concat("deployments/%s")),

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