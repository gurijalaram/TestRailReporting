package com.apriori.qms.models.response.scenariodiscussion;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUserEnablements;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUserProfile;
import com.apriori.qms.models.response.bidpackage.CustomAttributes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioProjectUserInformation {
    private Boolean isSystemUser;
    private String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String customerIdentity;
    private BidPackageProjectUserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomAttributes customAttributes;
    private String userType;
    private String avatarColor;
    private Boolean hasCompleteProfile;
    private BidPackageProjectUserEnablements userEnablements;
}
