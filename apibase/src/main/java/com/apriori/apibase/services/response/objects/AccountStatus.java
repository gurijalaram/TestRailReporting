package com.apriori.apibase.services.response.objects;

import com.apriori.annotations.Schema;

import lombok.Data;

@Schema(location = "AccountsStatusSchema.json")
@Data
public class AccountStatus {
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
}
