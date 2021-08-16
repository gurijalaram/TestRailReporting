package com.apriori.apibase.services.nts.objects;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "NtsSendEmailResponseSchema.json")
public class SendEmailResponse {
    private String identity;

    public SendEmailResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getIdentity() {
        return identity;
    }
}
