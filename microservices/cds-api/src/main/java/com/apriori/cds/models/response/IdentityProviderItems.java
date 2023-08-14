
package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IdentityProviderSchema.json")
@Data
public class IdentityProviderItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private IdentityProviderPlatform identityProviderPlatform;
    private Boolean active;
    private String name;
    private String displayName;
    private String description;
    private List<String> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private AttributeMappings attributeMappings;
    private String signRequestAlgorithm;
    private String signRequestAlgorithmDigest;
    private String protocolBinding;
}
