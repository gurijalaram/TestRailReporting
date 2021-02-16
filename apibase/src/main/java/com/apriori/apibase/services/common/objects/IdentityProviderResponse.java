package com.apriori.apibase.services.common.objects;

import com.apriori.apibase.services.cas.AttributeMappings;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
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

    public IdentityProviderResponse getResponse() {
        return response;
    }

    public IdentityProviderResponse setResponse(IdentityProviderResponse response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public IdentityProviderResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IdentityProviderResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public IdentityProviderResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public IdentityProviderResponse setIdentityProviderPlatform(IdentityProviderPlatform identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public IdentityProviderResponse setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdentityProviderResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public IdentityProviderResponse setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdentityProviderResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getIdpDomains() {
        return idpDomains;
    }

    public IdentityProviderResponse setIdpDomains(List<String> idpDomains) {
        this.idpDomains = idpDomains;
        return this;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public IdentityProviderResponse setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
        return this;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public IdentityProviderResponse setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
        return this;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public IdentityProviderResponse setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
        return this;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public IdentityProviderResponse setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
        return this;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public IdentityProviderResponse setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
        return this;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public IdentityProviderResponse setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
        return this;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public IdentityProviderResponse setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
        return this;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public IdentityProviderResponse setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
        return this;
    }
}
