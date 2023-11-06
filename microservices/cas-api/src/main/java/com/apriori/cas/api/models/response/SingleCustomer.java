package com.apriori.cas.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SingleCustomerSchema.json")
@JsonRootName("response")
@Data
public class SingleCustomer {
    private String identity;
    private String createdAt;
    private String createdBy;
    private String name;
    private String cloudReference;
    private String description;
    private String customerType;
    private String salesforceId;
    private Boolean active;
    private Integer maxCadFileRetentionDays;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<Object> oneTimePasswordApplications = null;
    private String createdByName;
    private List<Object> identityProviders = null;
    private List<String> emailDomains = null;
    private String authenticationType;
}
