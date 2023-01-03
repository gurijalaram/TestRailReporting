package com.apriori.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersData {
    private String loginID;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String isAdmin;
    private String isVPEAdmin;
    private String isJasperAdmin;
    @JsonProperty("AppStream")
    private String appStream;
    @JsonProperty("ReportUser")
    private String reportUser;
    private String defaultPassword;
    private String resetPassword;
    private String userLicenseName;
    private String preferredCurrency;
    private String schemaPrivileges;
    private String defaultSchema;
    private String roles;
    private String defaultRole;
    private String roleName;
    private String applicationList;
    private String prefix;
    private String suffix;
    private String jobTitle;
    private String department;
    @JsonProperty("city/town")
    private String cityTown;
    @JsonProperty("state/province")
    private String stateProvince;
    private String county;
    private String countryCode;
    private String timezone;
}
