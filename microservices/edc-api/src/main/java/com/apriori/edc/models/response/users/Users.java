package com.apriori.edc.models.response.users;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "UsersResponse.json")
@JsonRootName("response")
@Data
public class Users {
    private Boolean isSystemUser;
    private String identity;
    private String createdBy;
    private String updatedBy;
    private String customerIdentity;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomAttributes customAttributes;
    private String userType;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}

