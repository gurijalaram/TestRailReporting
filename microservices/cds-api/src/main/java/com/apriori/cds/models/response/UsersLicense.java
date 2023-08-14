package com.apriori.cds.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersLicense {
    private String customerIdentity;
    private String userIdentity;
    private String siteName;
    private String siteIdentity;
    private String licenseIdentity;
    private String licenseDescription;
    private String subLicenseName;
    private String subLicenseIdentity;
}