package com.apriori.nts.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Schema(location = "NtsSendEmailResponseSchema.json")
@Data
public class SendEmail {
    private String identity;
}
