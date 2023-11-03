package com.apriori.cas.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Accessors(fluent = true)
public class IdentityHolder {
    private String accessControlIdentity;
    private String deploymentIdentity;
    private String customerIdentity;
    private String siteIdentity;
    private String licenseIdentity;
    private String installationIdentity;
    private String subLicenseIdentity;
    private String userIdentity;
}