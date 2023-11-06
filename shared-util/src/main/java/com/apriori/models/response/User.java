package com.apriori.models.response;

import com.apriori.annotations.CreatableModel;
import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UserSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@CreatableModel("user")
@JsonRootName("response")
public class User {
    protected Boolean isSystemUser;
    protected String identity;
    protected String userType;
    protected UserProfile userProfile;
    protected String email;
    protected String username;
    protected Boolean active;
    protected List<UserSite> sites;
    protected CustomAttributes customAttributes;
    protected List<Roles> roles;
    protected String customerIdentity;
    protected Boolean mfaRequired;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime createdAt;
    protected String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime updatedAt;
    protected String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime deletedAt;
    protected String deletedBy;
    protected Boolean hasCompleteProfile;
    protected Enablements enablements;
    protected String mfaAuthenticator;
    protected List<Object> resourcesAllowedToCreate = null;
    protected CustomProperties customProperties;
    protected String createdByName;
    protected String updatedByName;
    protected List<Object> licenseAssignments = null;
}
