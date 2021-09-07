package com.apriori.cas.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    GET_CUSTOMERS(String.format(PropertiesContext.get("${env}.cas.api_url"), "customers/")),
    GET_CUSTOMER("customers/"),

    //SITES
    VALIDATE_SITES("/sites/validate"),
    POST_SITES("/sites"),

    //USERS
    POST_USERS("/users/"),

    //BATCHES
    POST_BATCHES("/batches/"),
    POST_BATCH_ITEMS("/items/"),

    //APPLICATIONS
    GET_CUSTOMER_APPLICATIONS(GET_CUSTOMERS.getEndpointString() + PropertiesContext.get("${env}.customer_identity") + "/applications"),

    //CONFIGURATIONS
    GET_CONFIGURATIONS(String.format(PropertiesContext.get("${env}.cas.api_url"), "configurations/ap-versions")),

    //CUSTOMER DEPLOYMENTS
    GET_CUSTOMER_DEPLOYMENT(GET_CUSTOMERS.getEndpointString() + PropertiesContext.get("${env}.customer_identity") + "/deployments"),
    GET_CUSTOMER_DEPLOYMENT_ID(GET_CUSTOMERS.getEndpointString() + PropertiesContext.get("${env}.customer_identity") + "/deployments/"),

    //IDENTITY PROVIDERS
    GET_IDENTITY_PROVIDERS(GET_CUSTOMERS.getEndpointString() + PropertiesContext.get("${env}.customer_identity") + "/identity-providers/"),

    //CURRENT USER
    GET_CURRENT_USER(String.format(PropertiesContext.get("${env}.cas.api_url"), "users/current"));

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
        return PropertiesContext.get("${env}.cas.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}