package com.apriori.apibase.services.common.objects;

import com.apriori.apibase.services.cds.AttributeMappings;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "common/IdentityProviderRequestSchema.json")
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

    public IdentityProviderRequest getIdentityProvider() {
        return identityProvider;
    }

    public IdentityProviderRequest setIdentityProvider(IdentityProviderRequest identityProvider) {
        this.identityProvider = identityProvider;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public IdentityProviderRequest setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getIdentityProviderType() {
        return identityProviderType;
    }

    public IdentityProviderRequest setIdentityProviderType(String identityProviderType) {
        this.identityProviderType = identityProviderType;
        return this;
    }

    public String getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public IdentityProviderRequest setIdentityProviderPlatform(String identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public IdentityProviderRequest setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdentityProviderRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public IdentityProviderRequest setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdentityProviderRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getIdpDomains() {
        return idpDomains;
    }

    public IdentityProviderRequest setIdpDomains(List<String> idpDomains) {
        this.idpDomains = idpDomains;
        return this;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public IdentityProviderRequest setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
        return this;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public IdentityProviderRequest setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
        return this;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public IdentityProviderRequest setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
        return this;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public IdentityProviderRequest setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
        return this;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public IdentityProviderRequest setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
        return this;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public IdentityProviderRequest setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
        return this;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public IdentityProviderRequest setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
        return this;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public IdentityProviderRequest setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IdentityProviderRequest setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
