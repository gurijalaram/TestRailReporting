package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SubLicenseAssociationUsersSchema.json")
@Data
public class SubLicenseAssociationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private List<UserSite> sites = null;
    private CustomAttributes customAttributes;
    private String userType;
    private Boolean mfaRequired;
    private String customerIdentity;
    private List<String> roles;
    private Boolean hasCompleteProfile;
}