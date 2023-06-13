package com.apriori.cusapi.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class UserProfile {
    String familyName;
    String givenName;
    String createdBy;
    String countryCode;
    String prefix;
    String suffix;
    String jobTitle;
    String department;
    String townCity;
    String county;
    String officePhoneCountryCode;
    String officePhoneNumber;
    String timezone;
}
