package com.apriori.shared.util.email;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;

public enum EmailEnum implements ExternalEndpointEnum {
    //USER
    EMAIL_TOKEN("https://login.microsoftonline.com/%s/oauth2/v2.0/token"),
    EMAIL_MESSAGES("https://graph.microsoft.com/v1.0/me/messages"),
    EMAIL_MESSAGE("https://graph.microsoft.com/v1.0/me/messages/%s"),
    EMAIL_MESSAGE_ATTACHMENTS("https://graph.microsoft.com/v1.0/me/messages/%s/attachments");

    private final String endpoint;

    EmailEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return String.format(getEndpointString(), variables);
    }
}