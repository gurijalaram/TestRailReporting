package com.apriori.ats.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(location = "common/AtsAuthorizeSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("response")
@Builder
@Data
public class AuthorizationResponse {
    private Boolean isSystemUser;
    private String identity;
    private String createdBy;
    private String awsRole;
    private String userType;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private CustomAttributes customAttributes;
    private String customerIdentity;
    private String updatedBy;
    private Boolean mfaRequired;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
