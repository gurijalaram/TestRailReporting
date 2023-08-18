package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UserSchema.json")
@JsonRootName("response")
@Data
public class User {
    private Boolean isSystemUser;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String customerIdentity;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomAttributes customAttributes;
    private CustomProperties customProperties;
    private String userType;
    private List<String> resourcesAllowedToCreate;
    private Boolean hasCompleteProfile;
    private UserEnablements userEnablements;

    public static class CustomProperties {
    }

    @Data
    public static class CustomAttributes {
        private Integer workspaceId;
        private String defaultRole;
        private List<Object> roles;
        private String location;
        private String department;
    }

    @Data
    public static class UserEnablements {
        private Boolean connectAdminEnabled;
        private Boolean highMemEnabled;
        private Boolean previewEnabled;
        private Boolean sandboxEnabled;
        private Boolean userAdminEnabled;
    }
}