package com.apriori.ats.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "UserByEmailSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
public class UserByEmail {
    private Boolean isSystemUser;
    private String identity;
    private String customerIdentity;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private String mfaAuthenticator;
    private CustomAttributes customAttributes;
    private String awsRole;
    private String userType;
}