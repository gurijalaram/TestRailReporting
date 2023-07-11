package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.qms.entity.response.bidpackage.BidPackageProjectUserEnablements;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignee {
    public boolean isSystemUser;
    public String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime createdAt;
    public String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime updatedAt;
    public String updatedBy;
    public String customerIdentity;
    public UserProfile userProfile;
    public String email;
    public String username;
    public boolean active;
    public boolean mfaRequired;
    public CustomAttributes customAttributes;
    public String userType;
    public String avatarColor;
    public boolean hasCompleteProfile;
    private BidPackageProjectUserEnablements userEnablements;
}
