package com.apriori.qms.models.response.scenariodiscussion;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectUserEnablements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MentionedUser {
    private Boolean active;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private CustomAttributes customAttributes;
    private String customerIdentity;
    private String email;
    private String identity;
    private Boolean isSystemUser;
    private Boolean mfaRequired;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private UserProfile userProfile;
    private String userType;
    private String username;
    private Boolean hasCompleteProfile;
    private BidPackageProjectUserEnablements userEnablements;
}
