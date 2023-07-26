package com.apriori.cds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "common/IdentityProviderRequestSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IdentityProviderRequest {
    private IdentityProviderRequest identityProvider;
    private String contact;
    private String identityProviderType;
    private String identityProviderPlatform;
    private Boolean active;
    private String name;
    private String displayName;
    private String description;
    private List<String> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private String signRequestAlgorithm;
    private String signRequestAlgorithmDigest;
    private String protocolBinding;
    private AttributeMappings attributeMappings;
    private String createdBy;
    private String authenticationType;
}
