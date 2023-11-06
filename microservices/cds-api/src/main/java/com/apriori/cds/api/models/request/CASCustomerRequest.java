package com.apriori.cds.api.models.request;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CASCustomerRequestSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CASCustomerRequest {
    private String name;
    private String description;
    private String customerType;
    private String createdBy;
    private String salesforceId;
    private String cloudReference;
    private Boolean active;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private Integer maxCadFileRetentionDays;
    private Integer maxCadFileSize;
    private String mfaAuthenticator;
    private List<String> emailDomains;
}