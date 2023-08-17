package com.apriori.edc.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsRequest {
    private String accountId;
    private String name;
    private String accountSecret;
    private String type;
}
