package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.models.response.Enablements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ServiceAccountSchema.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccount {
    private Boolean isSystemUser;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String createdBy;
    private String customerIdentity;
    private CustomerUserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean hasCompleteProfile;
    private Boolean mfaRequired;
    private CustomAttributes customAttributes;
    private String createdByName;
    private String serviceAccount;
    private String status;
    private String userType;
    private List<String> roles;
    private Enablements enablements;
}