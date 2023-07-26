package com.apriori.nts.entity.response;

import com.apriori.annotations.Schema;

import lombok.Data;

@Schema(location = "NtsSendEmailResponseSchema.json")
@Data
public class SendEmail {
    private String identity;
}
