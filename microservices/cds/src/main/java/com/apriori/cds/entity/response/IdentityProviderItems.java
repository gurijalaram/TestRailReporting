
package com.apriori.cds.entity.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentityProviderItems {

    private String identity;
    private String createdBy;
    private String createdAt;
    private com.example.IdentityProviderPlatform identityProviderPlatform;
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
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getIdentity() {
        return identity;
    }

    public IdentityProviderItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IdentityProviderItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public IdentityProviderItems setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public com.example.IdentityProviderPlatform getIdentityProviderPlatform() {
        return identityProviderPlatform;
    }

    public IdentityProviderItems setIdentityProviderPlatform(com.example.IdentityProviderPlatform identityProviderPlatform) {
        this.identityProviderPlatform = identityProviderPlatform;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public IdentityProviderItems setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdentityProviderItems setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public IdentityProviderItems setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdentityProviderItems setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getIdpDomains() {
        return idpDomains;
    }

    public IdentityProviderItems setIdpDomains(List<String> idpDomains) {
        this.idpDomains = idpDomains;
        return this;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public IdentityProviderItems setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
        return this;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public IdentityProviderItems setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
        return this;
    }

    public String getSigningCertificateExpiresAt() {
        return signingCertificateExpiresAt;
    }

    public IdentityProviderItems setSigningCertificateExpiresAt(String signingCertificateExpiresAt) {
        this.signingCertificateExpiresAt = signingCertificateExpiresAt;
        return this;
    }

    public Boolean getSignRequest() {
        return signRequest;
    }

    public IdentityProviderItems setSignRequest(Boolean signRequest) {
        this.signRequest = signRequest;
        return this;
    }

    public AttributeMappings getAttributeMappings() {
        return attributeMappings;
    }

    public IdentityProviderItems setAttributeMappings(AttributeMappings attributeMappings) {
        this.attributeMappings = attributeMappings;
        return this;
    }

    public String getSignRequestAlgorithm() {
        return signRequestAlgorithm;
    }

    public IdentityProviderItems setSignRequestAlgorithm(String signRequestAlgorithm) {
        this.signRequestAlgorithm = signRequestAlgorithm;
        return this;
    }

    public String getSignRequestAlgorithmDigest() {
        return signRequestAlgorithmDigest;
    }

    public IdentityProviderItems setSignRequestAlgorithmDigest(String signRequestAlgorithmDigest) {
        this.signRequestAlgorithmDigest = signRequestAlgorithmDigest;
        return this;
    }

    public String getProtocolBinding() {
        return protocolBinding;
    }

    public IdentityProviderItems setProtocolBinding(String protocolBinding) {
        this.protocolBinding = protocolBinding;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public IdentityProviderItems setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
