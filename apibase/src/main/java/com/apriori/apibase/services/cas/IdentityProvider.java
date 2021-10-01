package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.IdentityProviderPlatform;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IdentityProviderSchema.json")
public class IdentityProvider {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean active;
    private String name;
    private IdentityProviderPlatform identityProviderPlatform;
    private String displayName;
    private String description;
    private List<Object> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private AttributeMappings attributeMappings;
    private String createdByName;
    private String signRequestAlgorithm;
    private String signRequestAlgorithmDigest;
    private String protocolBinding;

    public String getIdentity() {
        return identity;
    }

    public IdentityProvider setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public IdentityProvider setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IdentityProvider setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public IdentityProvider setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdentityProvider setName(String name) {
        this.name = name;
        return this;
    }

    public IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public IdentityProvider setIdentityProviderPlatform(IdentityProviderPlatform identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public IdentityProvider setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdentityProvider setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Object> getIdpDomains() {
        return idpDomains;
    }

    public IdentityProvider setIdpDomains(List<Object> idpDomains) {
        this.idpDomains = idpDomains;
        return this;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public IdentityProvider setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
        return this;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public IdentityProvider setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
        return this;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public IdentityProvider setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
        return this;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public IdentityProvider setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
        return this;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public IdentityProvider setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public IdentityProvider setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public IdentityProvider setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
        return this;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public IdentityProvider setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
        return this;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public IdentityProvider setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
        return this;
    }
}
