package com.apriori.cus.models.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserProfile {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String createdBy;
    private String givenName;
    private String familyName;
    private String countryCode;
    private String prefix;
    private String suffix;
    private String jobTitle;
    private String department;
    private String townCity;
    private String county;
    private String officePhoneCountryCode;
    private String officePhoneNumber;
    private String timezone;
}
