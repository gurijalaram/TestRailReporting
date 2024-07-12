package com.apriori.ats.api.utils.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum ATSAPIEnum implements ExternalEndpointEnum {
    //TOKENS
    TOKEN("/tokens"),

    //USERS
    USER_PASSWORD_BY_EMAIL("/users/%s/password"),
    USER_BY_EMAIL("/users/%s"),
    USER_MFA("/users/%s/reset-mfa"),

    //AUTHENTICATION
    AUTHENTICATE("/authenticate"),
    SAML_PROVIDERS("/upsertSamlUser"),

    //VERIFY
    EMAIL_DOMAINS("/verify/email-domains"),

    //CUSTOMERS
    CUSTOMER_USERS_MFA("/customers/%s/reset-mfa"),

    //AUTHORIZATION
    POST_AUTHORIZE_BY_BASE_URL_SECRET("/authorize"),
    CLOUD_CONTEXT("/context/%s");

    private final String endpoint;

    ATSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("ats.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
