package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.IdentityProviderPlatform;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/SingleIdpSchema.json")
public class SingleIdp {
    private SingleIdp response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean active;
    private String name;
    private IdentityProviderPlatform identityProviderPlatform;
    private String displayName;
    private String description;
    private List<String> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private AttributeMappings attributeMappings;
    private String createdByName;
    private String protocolBinding;
    private String signRequestAlgorithm;
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

    public SingleIdp setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SingleIdp setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SingleIdp setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public SingleIdp setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public SingleIdp setName(String name) {
        this.name = name;
        return this;
    }

    public IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public SingleIdp setIdentityProviderPlatform(IdentityProviderPlatform identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SingleIdp setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SingleIdp setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getIdpDomains() {
        return idpDomains;
    }

    public SingleIdp setIdpDomains(List<String> idpDomains) {
        this.idpDomains = idpDomains;
        return this;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public SingleIdp setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
        return this;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public SingleIdp setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
        return this;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public SingleIdp setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
        return this;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public SingleIdp setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
        return this;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public SingleIdp setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public SingleIdp setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public SingleIdp setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
        return this;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public SingleIdp setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
        return this;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public SingleIdp setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
        return this;
    }
}
