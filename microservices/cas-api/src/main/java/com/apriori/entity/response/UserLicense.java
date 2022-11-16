package com.apriori.entity.response;

import lombok.Data;

@Data
public class UserLicense {
    private String customerIdentity;
    private String userIdentity;
    private String siteName;
    private String siteIdentity;
    private String licenseIdentity;
    private String licenseDescription;
    private String subLicenseName;
    private String subLicenseIdentity;
}