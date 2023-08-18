package com.apriori.ats.utils.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum ATSAPIEnum implements ExternalEndpointEnum {
    TOKEN("tokens"),
    USER_PASSWORD_BY_EMAIL("users/%s/password"),
    USER_BY_EMAIL("users/%s"),
    AUTHENTICATE("authenticate"),
    SAML_PROVIDERS("upsertSamlUser"),
    USER_MFA("users/%s/reset-mfa"),
    CUSTOMER_USERS_MFA("customers/%s/reset-mfa"),
    CLOUD_CONTEXT("context/%s");

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
