package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

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
public class UserProfile {
    private String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String givenName;
    private String familyName;
    private String jobTitle;
    private String department;
    private String townCity;
    private String county;
    private String timezone;
    private String stateProvince;
    private String countryCode;
    private String supervisor;
    private String prefix;
    private String suffix;
    private String officePhoneCountryCode;
    private String officePhoneNumber;
}
