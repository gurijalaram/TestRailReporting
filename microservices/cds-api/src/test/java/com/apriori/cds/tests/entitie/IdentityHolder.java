package com.apriori.cds.tests.entitie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Accessors(fluent = true)
public class IdentityHolder {
    private String customerIdentity;
    private String siteIdentity;
    private String licenseIdentity;
    private String subLicenseIdentity;
    private String userIdentity;
}
