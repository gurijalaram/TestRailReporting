package com.apriori.ach.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum ACHAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers"),

    //USERS
    CUSTOMER_USERS("customers/%s/users"),
    USER_BY_ID("customers/%s/users/%s"),

    //USER PREFERENCES
    USER_PREFERENCES("users/current/preferences"),

    //USERS
    USER("users/current"),

    //PEOPLE
    PEOPLE("people"),

    //NOTIFICATIONS
    NOTIFICATIONS(CUSTOMERS.getEndpointString().concat("/%s/deployments/%s/notifications")),

    //APPLICATION METADATA
    APP_METADATA("application-metadata"),

    //ENABLEMENTS_SUPPORT
    ENABLEMENTS_SUPPORT("enablements-support");


    private final String endpoint;

    ACHAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.ach.api_url") + String.format(getEndpointString(), variables);
    }
}