package com.apriori.edcapi.entity.response.accounts;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "AccountsResponse.json")
@Data
@JsonRootName(value = "response")
public class AccountsResponse {
    private Boolean isValid;
    private Boolean isActive;
    private String identity;
    private String name;
    private String type;
    private String accountId;
    private String accountSecret;
    private Integer licenseLimit;
    private Integer licenseUsage;
    private String validFrom;
    private String validTo;
    private String licenseRefreshedAt;
}
