package com.apriori.cas.models.response;

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
@Schema(location = "CustomerUserSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CreatableModel("user")
@JsonRootName("response")
public class CustomerUser {
    private Boolean isSystemUser;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String customerIdentity;
    private CustomerUserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomAttributes customAttributes;
    private CustomProperties customProperties;
    private String createdByName;
    private String updatedByName;
    private List<Object> licenseAssignments = null;
    private String userType;
    private UserEnablements userEnablements;
    private Boolean hasCompleteProfile;
}
