package com.apriori.apibase.services.cas;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "cas/SingleIdpSchema.json")
public class SingleIdp {
    @JsonProperty
    private SingleIdp response;
    @JsonProperty
    private String identity;
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private String name;
    @JsonProperty
    private IdentityProviderPlatform identityProviderPlatform;
    @JsonProperty
    private String displayName;
    @JsonProperty
    private String description;
    @JsonProperty
    private List<Object> idpDomains = null;
    @JsonProperty
    private String signInUrl;
    @JsonProperty
    private String signingCertificate;
    @JsonProperty
    private String signingCertificateExpiresAt;
    @JsonProperty
    private Boolean signRequest;
    @JsonProperty
    private AttributeMappings attributeMappings;
    @JsonProperty
    private String createdByName;
    @JsonProperty
    private String protocolBinding;
    @JsonProperty
    private String signRequestAlgorithm;
    @JsonProperty
    private String signRequestAlgorithmDigest;

    public SingleIdp getResponse() {
        return response;
    }

    public SingleIdp setResponse(SingleIdp response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public void setIdentityProviderPlatform(IdentityProviderPlatform identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getIdpDomains() {
        return idpDomains;
    }

    public void setIdpDomains(List<Object> idpDomains) {
        this.idpDomains = idpDomains;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public void setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public void setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public void setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public void setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public void setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public void setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public void setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
    }
}
