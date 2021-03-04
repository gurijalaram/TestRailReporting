package com.apriori.apibase.services.common.objects;

import com.apriori.apibase.services.cds.AttributeMappings;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "common/IdentityProviderResponseSchema.json")
public class IdentityProviderResponse {
    private IdentityProviderResponse response;
    private String identity;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public IdentityProviderResponse setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public IdentityProviderResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public IdentityProviderResponse getResponse() {
        return response;
    }

    public String getIdentity() {
        return identity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public Boolean getActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIdpDomains() {
        return idpDomains;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }
}
